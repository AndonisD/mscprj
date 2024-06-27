package uk.ac.kcl.mscPrj.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.kcl.mscPrj.dto.UserDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<AbstractResponse> registerUser(@Valid @RequestBody UserDTO user, HttpServletRequest request) {
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        AbstractResponse response = userService.registerUser(user, appUrl);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<AbstractResponse> verifyEmail(@RequestParam("token") String verificationToken) {
        AbstractResponse response = userService.verifyEmail(verificationToken);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
