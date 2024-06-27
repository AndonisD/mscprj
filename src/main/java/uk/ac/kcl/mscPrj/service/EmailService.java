package uk.ac.kcl.mscPrj.service;

import org.springframework.mail.SimpleMailMessage;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface EmailService {
    AbstractResponse sendConfirmationEmail(SimpleMailMessage email);
}
