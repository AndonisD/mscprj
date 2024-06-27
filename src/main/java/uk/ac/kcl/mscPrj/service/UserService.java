package uk.ac.kcl.mscPrj.service;

import org.springframework.http.ResponseEntity;
import uk.ac.kcl.mscPrj.dto.UserDTO;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;

public interface UserService {
    AbstractResponse registerUser(UserDTO user, String appUrl);

    AbstractResponse verifyEmail(String confirmationToken);
}
