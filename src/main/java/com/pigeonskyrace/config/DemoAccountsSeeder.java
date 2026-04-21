package com.pigeonskyrace.config;

import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DemoAccountsSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDemoRoleAccounts() {
        return args -> {
            List<SeedUser> seedUsers = List.of(
                    new SeedUser("admin@pigeonskyrace.com", "Admin123!", "Platform Admin", Role.ADMIN),
                    new SeedUser("organizer@pigeonskyrace.com", "Organizer123!", "Race Organizer", Role.ORGANIZER),
                    new SeedUser("breeder@pigeonskyrace.com", "Breeder123!", "Demo Breeder", Role.BREEDER)
            );

            for (SeedUser seed : seedUsers) {
                User user = userRepository.findByEmail(seed.email()).orElseGet(User::new);
                user.setEmail(seed.email());
                user.setName(seed.name());
                user.setRole(seed.role());
                if (user.getPassword() == null || user.getPassword().isBlank()
                        || !passwordEncoder.matches(seed.rawPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(seed.rawPassword()));
                }
                userRepository.save(user);
            }
            log.info("Demo role accounts ensured: admin, organizer, breeder");
        };
    }

    private record SeedUser(String email, String rawPassword, String name, Role role) {
    }
}
