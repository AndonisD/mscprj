package uk.ac.kcl.mscPrj.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class EmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {
    @Value("${allowed-email-domains}")
    private List<String> allowedEmailDomains;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        String domain = email.substring(email.indexOf("@") + 1);
        return allowedEmailDomains.contains(domain);
    }
}
