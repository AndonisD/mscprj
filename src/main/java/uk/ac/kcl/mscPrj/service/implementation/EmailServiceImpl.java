package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    @Override
    public AbstractResponse sendConfirmationEmail(SimpleMailMessage email) {
        try {
            javaMailSender.send(email);
        } catch (MailSendException mailSendException){
            return new StatusResponse("Error: failed to send email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new StatusResponse("Success. Please check your email.", HttpStatus.CREATED);
    }
}
