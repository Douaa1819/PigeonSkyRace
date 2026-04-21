package com.pigeonskyrace.config;

import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.model.enums.Sexe;
import com.pigeonskyrace.repository.ColombierRepository;
import com.pigeonskyrace.repository.CompetionRepository;
import com.pigeonskyrace.repository.PigeonRepository;
import com.pigeonskyrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DemoAccountsSeeder {

    private final UserRepository userRepository;
    private final ColombierRepository colombierRepository;
    private final PigeonRepository pigeonRepository;
    private final CompetionRepository competionRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDemoRoleAccounts() {
        return args -> {
            List<SeedUser> seedUsers = List.of(
                    new SeedUser("admin@test.com", "12345678", "System Admin", Role.ADMIN),
                    new SeedUser("organizer@test.com", "12345678", "Race Organizer", Role.ORGANIZER),
                    new SeedUser("breeder1@test.com", "12345678", "Breeder One", Role.BREEDER),
                    new SeedUser("breeder2@test.com", "12345678", "Breeder Two", Role.BREEDER)
            );

            User breeder1 = null;
            User breeder2 = null;
            for (SeedUser seed : seedUsers) {
                User user = userRepository.findByEmail(seed.email()).orElseGet(User::new);
                user.setEmail(seed.email());
                user.setName(seed.name());
                user.setRole(seed.role());
                if (user.getPassword() == null || user.getPassword().isBlank()
                        || !passwordEncoder.matches(seed.rawPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(seed.rawPassword()));
                }
                User saved = userRepository.save(user);
                if ("breeder1@test.com".equals(saved.getEmail())) {
                    breeder1 = saved;
                } else if ("breeder2@test.com".equals(saved.getEmail())) {
                    breeder2 = saved;
                }
            }

            if (breeder1 != null) {
                Colombier atlas = ensureLoft("Atlas Loft", 33.5731, -7.5898, breeder1);
                ensurePigeon("MA-001", Sexe.MALE, 2, "Blue", atlas);
            }
            if (breeder2 != null) {
                Colombier sahara = ensureLoft("Sahara Loft", 31.6295, -7.9811, breeder2);
                ensurePigeon("MA-002", Sexe.FEMALE, 1, "White", sahara);
            }

            ensureCompetition("Rabat Speed Race", 34.020882, -6.841650, 120);
            ensureCompetition("Atlas Long Distance", 31.791702, -7.092620, 600);

            log.info("Demo seed ensured: users, lofts, pigeons, competitions");
        };
    }

    private Colombier ensureLoft(String name, double lat, double lng, User owner) {
        Optional<Colombier> existing = colombierRepository.findByNomColombier(name);
        Colombier loft = existing.orElseGet(Colombier::new);
        loft.setNomColombier(name);
        loft.setCoordonneeGPSlatitude(lat);
        loft.setCoordonneeGPSlongitude(lng);
        loft.setUser(owner);
        return colombierRepository.save(loft);
    }

    private void ensurePigeon(String ring, Sexe sexe, int age, String color, Colombier loft) {
        Pigeon pigeon = pigeonRepository.findByNumeroBague(ring).orElseGet(Pigeon::new);
        pigeon.setNumeroBague(ring);
        pigeon.setSexe(sexe);
        pigeon.setAge(age);
        pigeon.setCouleur(color);
        pigeon.setDateNaissance(LocalDate.now().minusYears(age));
        pigeon.setColombier(loft);
        pigeonRepository.save(pigeon);
    }

    private void ensureCompetition(String name, double lat, double lng, int distanceKm) {
        Competion race = competionRepository.findByNom(name).orElseGet(Competion::new);
        race.setNom(name);
        race.setLatitudeGPS(lat);
        race.setLongitudeGPS(lng);
        race.setNombrePigeons(2);
        race.setStartTime(LocalDateTime.now().minusHours(distanceKm > 200 ? 8 : 2));
        race.setEndTime(LocalDateTime.now().plusHours(distanceKm > 200 ? 10 : 3));
        competionRepository.save(race);
    }

    private record SeedUser(String email, String rawPassword, String name, Role role) {
    }
}
