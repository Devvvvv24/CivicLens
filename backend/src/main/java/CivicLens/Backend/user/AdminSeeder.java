package CivicLens.Backend.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    public AdminSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (adminEmail == null ||
                adminEmail.isBlank() ||
                adminPassword == null ||
                adminPassword.isBlank()) {

            return;
        }

        if (!userRepository.existsByEmail(adminEmail)) {

            AppUser admin = new AppUser(
                    "CivicLens Admin",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN
            );

            userRepository.save(admin);
        }
    }
}