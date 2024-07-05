package uk.ac.kcl.mscPrj.service;

import org.springframework.security.core.Authentication;
import uk.ac.kcl.mscPrj.dto.PostDTO;
import uk.ac.kcl.mscPrj.dto.ReplyDTO;
import uk.ac.kcl.mscPrj.dto.VoteDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface PostService {
    AbstractResponse addNewPost(PostDTO post, Authentication authentication); //FIXME: Authentication authentication

    AbstractResponse addNewReply(ReplyDTO reply, Authentication authentication);

    AbstractResponse rateReply(VoteDTO voteDTO, Authentication authentication);
}
