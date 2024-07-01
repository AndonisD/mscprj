package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String emailId);

    Boolean existsByEmail(String email);
}
