package uk.ac.kcl.mscPrj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.kcl.mscPrj.entity.Post;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.PostService;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AbstractResponse> addNewPost(@RequestBody Post post) { //FIXME:  Authentication authentication
        AbstractResponse response = postService.addNewPost(post);//FIXME:  authentication
        return new ResponseEntity<>(response, response.getStatus());
    }
}
