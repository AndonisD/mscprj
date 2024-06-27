package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.PostDTO;
import uk.ac.kcl.mscPrj.entity.Post;
import uk.ac.kcl.mscPrj.entity.User;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.PostRepository;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.service.PostService;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public AbstractResponse addNewPost(PostDTO post) {
        User poster = userRepository.findAll().getLast(); //FIXME
        Post newPost = new Post(post.getBody(), poster, post.getIsAnonymous());
        postRepository.save(newPost);
        return new StatusResponse("Post submitted successfully", HttpStatus.CREATED);
    }
}
