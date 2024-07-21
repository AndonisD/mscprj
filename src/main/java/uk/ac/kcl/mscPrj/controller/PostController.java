package uk.ac.kcl.mscPrj.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uk.ac.kcl.mscPrj.dto.SubmitPostDTO;
import uk.ac.kcl.mscPrj.dto.SubmitReplyDTO;
import uk.ac.kcl.mscPrj.dto.RateReplyDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.PostService;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/submitPost")
    public ResponseEntity<AbstractResponse> addNewPost(@Valid @RequestBody SubmitPostDTO post, Authentication authentication) {
        AbstractResponse response = postService.addNewPost(post, authentication);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/getPost")
    public ResponseEntity<AbstractResponse> getUserByID(@RequestParam("id") Long id) throws IOException {
        AbstractResponse response = postService.getPost(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/submitReply")
    public ResponseEntity<AbstractResponse> addNewReply(@Valid @RequestBody SubmitReplyDTO reply, Authentication authentication) {
        AbstractResponse response = postService.addNewReply(reply, authentication);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/rateReply")
    public ResponseEntity<AbstractResponse> rateReply(@Valid @RequestBody RateReplyDTO rateReplyDTO, Authentication authentication) {
        AbstractResponse response = postService.rateReply(rateReplyDTO, authentication);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
