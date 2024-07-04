package uk.ac.kcl.mscPrj.service.implementation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.AuthenticationRequest;
import uk.ac.kcl.mscPrj.dto.RegistrationRequest;
import uk.ac.kcl.mscPrj.model.VerificationToken;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.VerificationTokenRepository;
import uk.ac.kcl.mscPrj.security.JwtUtil;
import uk.ac.kcl.mscPrj.service.EmailService;
import uk.ac.kcl.mscPrj.service.UserService;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  EmailService emailService;
    @Autowired
    private  VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    public AbstractResponse registerUser(RegistrationRequest user, String appUrl) {

        User newUser;

        if (userRepository.existsByEmail(user.getEmail())) {
            User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
            if (existingUser.isVerified()){
                return new StatusResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
            } else {
                newUser = existingUser;
            }
        }else {
            String password = passwordEncoder.encode(user.getPassword());
            newUser = new User(user.getUsername(), user.getEmail(), password);
        }

        userRepository.save(newUser);

        VerificationToken verificationToken;

        if(verificationTokenRepository.existsByUser(newUser)){
            verificationToken = verificationTokenRepository.findByUser(newUser);
        }else {
            verificationToken = new VerificationToken(newUser);
            verificationTokenRepository.save(verificationToken);
        }

        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setTo(user.getEmail());
        confirmationEmail.setSubject("Complete Registration");
        confirmationEmail.setText("Click on this link to complete your registration: "
                + appUrl + "/api/users/verifyEmail?token="+ verificationToken.getVerificationToken());

        return emailService.sendConfirmationEmail(confirmationEmail);
    }

    @Override
    public AbstractResponse verifyEmail(String verificationToken) {

        VerificationToken token = verificationTokenRepository.findByVerificationToken(verificationToken);
        System.out.println("yeet");

        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setVerified(true);
            userRepository.save(user);
            return new StatusResponse("Email verified. You may now login.", HttpStatus.CREATED);
        }
        return new StatusResponse("Error: Couldn't verify email.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    public AbstractResponse loginUser(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new StatusResponse("Error: Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return new StatusResponse(token, HttpStatus.ACCEPTED);
    }
}
