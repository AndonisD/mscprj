package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.UserDTO;
import uk.ac.kcl.mscPrj.model.VerificationToken;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import uk.ac.kcl.mscPrj.repository.VerificationTokenRepository;
import uk.ac.kcl.mscPrj.service.EmailService;
import uk.ac.kcl.mscPrj.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public AbstractResponse registerUser(UserDTO user, String appUrl) {

        User newUser;

        if (userRepository.existsByEmail(user.getEmail())) {
            User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
            if (existingUser.isVerified()){
                return new StatusResponse("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
            } else {
                newUser = existingUser;
            }
        }else {
            newUser = new User(user.getUsername(), user.getEmail(), user.getPassword());
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
        return new StatusResponse("Error. Couldn't verify email.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
