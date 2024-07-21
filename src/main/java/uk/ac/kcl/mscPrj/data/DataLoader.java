package uk.ac.kcl.mscPrj.data;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uk.ac.kcl.mscPrj.dto.RegisterDTO;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.mscPrj.service.UserService;

@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
    private final UserService userService;

    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        userService.addAdmin(new RegisterDTO(env.getProperty("admin-username"), env.getProperty("admin-email"), env.getProperty("admin-password")));
    }
}
