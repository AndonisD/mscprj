package uk.ac.kcl.mscPrj.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.kcl.mscPrj.dto.PostDTO;
import uk.ac.kcl.mscPrj.dto.ReplyDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.PostService;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/submitPost")
    public ResponseEntity<AbstractResponse> addNewPost(@Valid @RequestBody PostDTO post, Authentication authentication) {
        AbstractResponse response = postService.addNewPost(post, authentication);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/submitReply")
    public ResponseEntity<AbstractResponse> addNewPost(@Valid @RequestBody ReplyDTO reply, Authentication authentication) {
        AbstractResponse response = postService.addNewReply(reply, authentication);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
