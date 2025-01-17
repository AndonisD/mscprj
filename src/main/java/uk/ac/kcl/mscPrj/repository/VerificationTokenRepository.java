package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByVerificationToken(String token);

    VerificationToken findByUser(User user);

    Boolean existsByUser(User user);

}
