package uk.ac.kcl.mscPrj.service;

import uk.ac.kcl.mscPrj.dto.PostDTO;
import uk.ac.kcl.mscPrj.dto.ReplyDTO;
import uk.ac.kcl.mscPrj.entity.Post;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface PostService {
    AbstractResponse addNewPost(PostDTO post); //FIXME: Authentication authentication

    AbstractResponse addNewReply(ReplyDTO reply);
}
