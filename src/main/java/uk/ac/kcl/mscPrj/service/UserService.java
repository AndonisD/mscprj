package uk.ac.kcl.mscPrj.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import uk.ac.kcl.mscPrj.dto.AuthenticationRequest;
import uk.ac.kcl.mscPrj.dto.RegistrationRequest;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface UserService extends UserDetailsService{
    AbstractResponse registerUser(RegistrationRequest user, String appUrl);

    AbstractResponse verifyEmail(String confirmationToken);

    AbstractResponse loginUser(AuthenticationRequest request);
}
