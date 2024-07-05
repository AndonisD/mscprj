package uk.ac.kcl.mscPrj.service;

import org.springframework.security.core.Authentication;
import uk.ac.kcl.mscPrj.dto.SubmitPostDTO;
import uk.ac.kcl.mscPrj.dto.SubmitReplyDTO;
import uk.ac.kcl.mscPrj.dto.RateReplyDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface PostService {
    AbstractResponse addNewPost(SubmitPostDTO post, Authentication authentication); //FIXME: Authentication authentication

    AbstractResponse addNewReply(SubmitReplyDTO reply, Authentication authentication);

    AbstractResponse rateReply(RateReplyDTO rateReplyDTO, Authentication authentication);
}
