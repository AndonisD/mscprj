package uk.ac.kcl.mscPrj.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.LoginDTO;
import uk.ac.kcl.mscPrj.dto.RegisterDTO;
import uk.ac.kcl.mscPrj.dto.GetUserDTO;
import uk.ac.kcl.mscPrj.model.VerificationToken;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.DataResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.RateRepository;
import uk.ac.kcl.mscPrj.repository.ReplyRepository;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.VerificationTokenRepository;
import uk.ac.kcl.mscPrj.security.JwtUtil;
import uk.ac.kcl.mscPrj.service.EmailService;
import uk.ac.kcl.mscPrj.service.UserService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private RateRepository rateRepository;
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

//    @Autowired
//    private ReputationMarkersConfig reputationConfig;

    @Value("#{${repuation-markers}}")
    private Map<String, String> repuationMarkers;


    @Override
    public AbstractResponse registerUser(RegisterDTO user, String appUrl) {

        User newUser;

        if (userRepository.existsByEmail(user.getEmail())) {
            User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
            if (existingUser.isVerified()){
                return new StatusResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST); //FIXME: https://medium.com/@aedemirsen/spring-boot-global-exception-handler-842d7143cf2a
            } else {
                newUser = existingUser;
            }
        }else {
            String password = passwordEncoder.encode(user.getPassword());
            newUser = new User(user.getUsername(), user.getEmail(), password);
        }

        //FIXME: need to check for username as well

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
                + appUrl + "/api/auth/verifyEmail?token="+ verificationToken.getVerificationToken());

        return emailService.sendConfirmationEmail(confirmationEmail);
    }

    public void addAdmin(RegisterDTO user) {

        if (userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername())){
            return;
        }

        String password = passwordEncoder.encode(user.getPassword());

        User newAdmin = new User(user.getUsername(), user.getEmail(), password, "ADMIN");

        userRepository.save(newAdmin);
    }

    @Override
    public AbstractResponse banUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return new StatusResponse("User not found", HttpStatus.NOT_FOUND);
        }
        User userUpdated = user.get();
        userUpdated.setVerified(false);
        userRepository.save(userUpdated);
        return new StatusResponse("User banned", HttpStatus.ACCEPTED);
    }

    @Override
    public AbstractResponse verifyEmail(String verificationToken) {

        VerificationToken token = verificationTokenRepository.findByVerificationToken(verificationToken);

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
                getAuthorities(user.getRole())
        );
    }

    private Set<GrantedAuthority> getAuthorities(String role) {
        return new HashSet<>(){{
            add(new SimpleGrantedAuthority("ROLE_" + role));
        }};
    }

    @Override
    public AbstractResponse loginUser(LoginDTO request) {
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

    @Override
    public AbstractResponse getUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isEmpty()){
            return new StatusResponse("User not found", HttpStatus.NOT_FOUND);
        }

        User user = optUser.get();

        int score = 0;

        score = replyRepository.findAllByPoster(user).stream()
                .flatMap(reply -> rateRepository.findAllByReply(reply).stream())
                .mapToInt(rating -> rating.getUpVote() ? 1 : -1)
                .sum();

        String reputation = getReputationMarker(score);

        GetUserDTO responseUser = new GetUserDTO(user.getUsername(), reputation);

        return new DataResponse(responseUser, HttpStatus.ACCEPTED);
    }

    private String getReputationMarker(int score) {
        return repuationMarkers.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(Integer.parseInt(entry.getKey()), (entry.getValue())))
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .filter(entry -> score >= entry.getKey())
                .map(Map.Entry::getValue)
                .reduce((first, second) -> second)
                .orElse("");
    }
}