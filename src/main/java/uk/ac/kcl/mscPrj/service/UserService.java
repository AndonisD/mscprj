package uk.ac.kcl.mscPrj.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import uk.ac.kcl.mscPrj.dto.LoginDTO;
import uk.ac.kcl.mscPrj.dto.RegisterDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface UserService extends UserDetailsService{
    AbstractResponse registerUser(RegisterDTO user, String appUrl);

    AbstractResponse verifyEmail(String confirmationToken);

    AbstractResponse loginUser(LoginDTO request);
}
