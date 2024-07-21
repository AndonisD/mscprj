package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.*;
import uk.ac.kcl.mscPrj.model.Post;
import uk.ac.kcl.mscPrj.model.Reply;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.Rating;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.DataResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.PostRepository;
import uk.ac.kcl.mscPrj.repository.ReplyRepository;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.RateRepository;
import uk.ac.kcl.mscPrj.service.PostService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final RateRepository rateRepository;

    @Override
    public AbstractResponse addNewPost(SubmitPostDTO post, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post newPost = new Post(post.getBody(), poster, post.getIsAnonymous());
        postRepository.save(newPost);
        return new StatusResponse("Post submitted successfully", HttpStatus.CREATED);
    }
    @Override
    public AbstractResponse addNewReply(SubmitReplyDTO reply, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post post = postRepository.getReferenceById(reply.getPostId());
        Reply newReply = new Reply(poster, post, reply.getBody());
        replyRepository.save(newReply);
        return new StatusResponse("Reply submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse rateReply(RateReplyDTO rateReplyDTO, Authentication authentication) {
        Reply reply = replyRepository.getReferenceById(rateReplyDTO.getReplyId());
        User voter = userRepository.findByUsername(authentication.getName());
        Rating existingingRating = rateRepository.findByReplyAndRater(reply, voter);

        if (existingingRating == null) {
            Rating newRating = new Rating(reply, voter, rateReplyDTO.getUpVote());
            rateRepository.save(newRating);
        } else if (existingingRating.getUpVote() != rateReplyDTO.getUpVote()) {
            existingingRating.setUpVote(rateReplyDTO.getUpVote());
            rateRepository.save(existingingRating);
        }

        return new StatusResponse("Rating submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse getPost(Long id) {
        Post post = postRepository.getReferenceById(id);
        List<Reply> replies = replyRepository.findAllByPost(post);
        List<GetReplyDTO> repliesDTO = new ArrayList<>();
        replies.forEach(reply -> {
            Integer score = rateRepository.findAllByReply(reply).stream().mapToInt(rating -> rating.getUpVote() ? 1 : -1).sum();
            repliesDTO.add(new GetReplyDTO(reply.getPoster().getUsername(), reply.getBody(), score));
        });

        String username = post.getIsAnonymous() ? "Anonymous" :  post.getPoster().getUsername();

        GetPostDTO responsePost = new GetPostDTO(username, post.getBody(), repliesDTO);

        return new DataResponse(responsePost, HttpStatus.CREATED);
    }
}
