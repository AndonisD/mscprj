package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.SubmitPostDTO;
import uk.ac.kcl.mscPrj.dto.SubmitReplyDTO;
import uk.ac.kcl.mscPrj.dto.RateReplyDTO;
import uk.ac.kcl.mscPrj.model.Post;
import uk.ac.kcl.mscPrj.model.Reply;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.Rating;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.PostRepository;
import uk.ac.kcl.mscPrj.repository.ReplyRepository;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.RateRepository;
import uk.ac.kcl.mscPrj.service.PostService;

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
}
