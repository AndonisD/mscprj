package uk.ac.kcl.mscPrj.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import uk.ac.kcl.mscPrj.dto.LoginDTO;
import uk.ac.kcl.mscPrj.dto.RegisterDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.UserService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ResourceLoader resourceLoader;

    @PostMapping("/register")
    public ResponseEntity<AbstractResponse> registerUser(@Valid @RequestBody RegisterDTO user, HttpServletRequest request) {
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        AbstractResponse response = userService.registerUser(user, appUrl);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String verificationToken) throws IOException {
        AbstractResponse response = userService.verifyEmail(verificationToken);

        Resource resource = (response.getStatus() == HttpStatus.CREATED) ?
                resourceLoader.getResource("classpath:/static/EmailVerificationSuccess.html"):
                resourceLoader.getResource("classpath:/static/EmailVerificationError.html");

        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return ResponseEntity.status(response.getStatus())
                .header("Content-Type", "text/html")
                .body(htmlContent);
    }

    @PostMapping("/login")
    public ResponseEntity<AbstractResponse> loginUser(@RequestBody LoginDTO request) {
        AbstractResponse response = userService.loginUser(request);

        return new ResponseEntity<>(response, response.getStatus());
    }

}
