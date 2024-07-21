package uk.ac.kcl.mscPrj.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.repository.UserRepository;

@AllArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    @Value("admin-username")
    private String username;

    @Value("admin-password")
    private String password;

    @Value("admin-email")
    private String email;

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(new User(username, email, password, "ADMIN"));
    }
}
