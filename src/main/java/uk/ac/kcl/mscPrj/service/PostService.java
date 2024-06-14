package uk.ac.kcl.mscPrj.service;

import uk.ac.kcl.mscPrj.entity.Post;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface PostService {
    AbstractResponse addNewPost(Post post); //FIXME: Authentication authentication
}
