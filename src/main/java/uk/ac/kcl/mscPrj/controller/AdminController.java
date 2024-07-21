package uk.ac.kcl.mscPrj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.PostService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PostService postService;

    @GetMapping("/getReports")
    public ResponseEntity<AbstractResponse> getReports(){
        AbstractResponse response = postService.getReports();

        return new ResponseEntity<>(response, response.getStatus());
    }
}
