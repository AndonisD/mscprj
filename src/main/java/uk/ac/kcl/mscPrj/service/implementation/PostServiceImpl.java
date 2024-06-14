package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.entity.Post;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.PostRepository;
import uk.ac.kcl.mscPrj.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public AbstractResponse addNewPost(Post post) { //FIXME: Authentication authentication
//        User user = userRepository.findByEmail(authentication.getName()).get();
//        post.setUser(user);
        postRepository.save(post);
        return new StatusResponse("Post submitted successfully", HttpStatus.CREATED);
    }
}
