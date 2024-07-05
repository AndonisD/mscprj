package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.PostDTO;
import uk.ac.kcl.mscPrj.dto.ReplyDTO;
import uk.ac.kcl.mscPrj.dto.VoteDTO;
import uk.ac.kcl.mscPrj.model.Post;
import uk.ac.kcl.mscPrj.model.Reply;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.Vote;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.PostRepository;
import uk.ac.kcl.mscPrj.repository.ReplyRepository;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.VoteRepository;
import uk.ac.kcl.mscPrj.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final VoteRepository voteRepository;

    @Override
    public AbstractResponse addNewPost(PostDTO post, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post newPost = new Post(post.getBody(), poster, post.getIsAnonymous());
        postRepository.save(newPost);
        return new StatusResponse("Post submitted successfully", HttpStatus.CREATED);
    }
    @Override
    public AbstractResponse addNewReply(ReplyDTO reply, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post post = postRepository.getReferenceById(reply.getPostId());
        Reply newReply = new Reply(poster, post, reply.getBody());
        replyRepository.save(newReply);
        return new StatusResponse("Reply submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse rateReply(VoteDTO voteDTO, Authentication authentication) {
        Reply reply = replyRepository.getReferenceById(voteDTO.getReplyId());
        User voter = userRepository.findByUsername(authentication.getName());
        Vote existingingVote = voteRepository.findByReplyAndVoter(reply, voter);

        if (existingingVote == null) {
            Vote newVote = new Vote(reply, voter, voteDTO.getUpVote());
            voteRepository.save(newVote);
        } else if (existingingVote.getUpVote() != voteDTO.getUpVote()) {
            existingingVote.setUpVote(voteDTO.getUpVote());
            voteRepository.save(existingingVote);
        }

        return new StatusResponse("Rating submitted successfully", HttpStatus.CREATED);
    }
}
