package com.pigeonskyrace.config;

import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.Resultat;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.model.SaisonPigeon;
import com.pigeonskyrace.model.User;
import com.pigeonskyrace.model.enums.Role;
import com.pigeonskyrace.model.enums.Sexe;
import com.pigeonskyrace.repository.ColombierRepository;
import com.pigeonskyrace.repository.CompetionRepository;
import com.pigeonskyrace.repository.PigeonSaisonCompetitionRepository;
import com.pigeonskyrace.repository.PigeonRepository;
import com.pigeonskyrace.repository.ResultatRepository;
import com.pigeonskyrace.repository.SaisonPigeonRepository;
import com.pigeonskyrace.repository.SaisonRepository;
import com.pigeonskyrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DemoAccountsSeeder {

    private final UserRepository userRepository;
    private final ColombierRepository colombierRepository;
    private final PigeonRepository pigeonRepository;
    private final CompetionRepository competionRepository;
    private final SaisonRepository saisonRepository;
    private final SaisonPigeonRepository saisonPigeonRepository;
    private final PigeonSaisonCompetitionRepository pigeonSaisonCompetitionRepository;
    private final ResultatRepository resultatRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDemoRoleAccounts() {
        return args -> {
            List<SeedUser> seedUsers = List.of(
                    new SeedUser("admin@pigeonskyrace.ma", "admin123", "System Admin", Role.ADMIN),
                    new SeedUser("organizer@pigeonskyrace.ma", "organizer123", "Race Organizer", Role.ORGANIZER),
                    new SeedUser("breeder@test.ma", "breeder123", "Breeder One", Role.BREEDER),
                    new SeedUser("breeder2@test.ma", "breeder2123", "Breeder Two", Role.BREEDER)
            );

            User breeder1 = null;
            User breeder2 = null;
            for (SeedUser seed : seedUsers) {
                String normalizedEmail = seed.email().trim().toLowerCase(Locale.ROOT);
                User user = userRepository.findByEmail(normalizedEmail).orElseGet(User::new);
                user.setEmail(normalizedEmail);
                user.setName(seed.name());
                user.setRole(seed.role());
                if (user.getPassword() == null || user.getPassword().isBlank()
                        || !passwordEncoder.matches(seed.rawPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(seed.rawPassword()));
                }
                User saved = userRepository.save(user);
                if ("breeder@test.ma".equalsIgnoreCase(saved.getEmail())) {
                    breeder1 = saved;
                } else if ("breeder2@test.ma".equalsIgnoreCase(saved.getEmail())) {
                    breeder2 = saved;
                }
            }

            Pigeon pigeon1 = null;
            Pigeon pigeon2 = null;
            if (breeder1 != null) {
                Colombier atlas = ensureLoft("Atlas Loft", 33.5731, -7.5898, breeder1);
                pigeon1 = ensurePigeon("MA-001", Sexe.MALE, 2, "Blue", atlas);
            }
            if (breeder2 != null) {
                Colombier sahara = ensureLoft("Sahara Loft", 31.6295, -7.9811, breeder2);
                pigeon2 = ensurePigeon("MA-002", Sexe.FEMALE, 1, "White", sahara);
            }

            Competion speedRace = ensureCompetition("Rabat Speed Race", 34.020882, -6.841650, 120);
            Competion longRace = ensureCompetition("Atlas Long Distance", 31.791702, -7.092620, 600);
            Saison saison = ensureSaison("2026 Federation Season", "Demo active season with seeded races.");

            bindCompetitionToSeason(saison, speedRace);
            bindCompetitionToSeason(saison, longRace);

            if (pigeon1 != null) {
                seedCompetitionParticipationAndResult(saison, pigeon1, speedRace, 129.7, 95.0, 1, 120);
                seedCompetitionParticipationAndResult(saison, pigeon1, longRace, 110.2, 88.0, 2, 600);
            }
            if (pigeon2 != null) {
                seedCompetitionParticipationAndResult(saison, pigeon2, speedRace, 121.4, 90.0, 2, 120);
                seedCompetitionParticipationAndResult(saison, pigeon2, longRace, 117.8, 96.0, 1, 600);
            }

            log.info("Demo seed ensured: users, lofts, pigeons, seasons, participations, competitions, results");
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

    private Pigeon ensurePigeon(String ring, Sexe sexe, int age, String color, Colombier loft) {
        Pigeon pigeon = pigeonRepository.findByNumeroBague(ring).orElseGet(Pigeon::new);
        pigeon.setNumeroBague(ring);
        pigeon.setSexe(sexe);
        pigeon.setAge(age);
        pigeon.setCouleur(color);
        pigeon.setDateNaissance(LocalDate.now().minusYears(age));
        pigeon.setColombier(loft);
        return pigeonRepository.save(pigeon);
    }

    private Competion ensureCompetition(String name, double lat, double lng, int distanceKm) {
        Competion race = competionRepository.findByNom(name).orElseGet(Competion::new);
        race.setNom(name);
        race.setLatitudeGPS(lat);
        race.setLongitudeGPS(lng);
        race.setNombrePigeons(2);
        race.setStartTime(LocalDateTime.now().minusHours(distanceKm > 200 ? 8 : 2));
        race.setEndTime(LocalDateTime.now().plusHours(distanceKm > 200 ? 10 : 3));
        return competionRepository.save(race);
    }

    private Saison ensureSaison(String nom, String description) {
        Saison saison = saisonRepository.findByNom(nom).orElseGet(Saison::new);
        saison.setNom(nom);
        saison.setDescription(description);
        saison.setDate(new Date());
        return saisonRepository.save(saison);
    }

    private void bindCompetitionToSeason(Saison saison, Competion competition) {
        List<Competion> comps = saison.getCompetions();
        if (comps == null) {
            comps = new java.util.ArrayList<>();
        }
        boolean exists = comps.stream().anyMatch(c -> c != null && c.getId() != null && c.getId().equals(competition.getId()));
        if (!exists) {
            comps.add(competition);
            saison.setCompetions(comps);
            saisonRepository.save(saison);
        }
        competition.setSaison(saison);
        competionRepository.save(competition);
    }

    private void seedCompetitionParticipationAndResult(
            Saison saison,
            Pigeon pigeon,
            Competion competition,
            double vitesse,
            double points,
            int classement,
            int distanceKm
    ) {
        SaisonPigeon saisonPigeon = saisonPigeonRepository.findBySaisonIdAndPigeonId(saison.getId(), pigeon.getId())
                .orElseGet(() -> {
                    SaisonPigeon created = new SaisonPigeon();
                    created.setSaison(saison);
                    created.setPigeon(pigeon);
                    return saisonPigeonRepository.save(created);
                });

        PigeonSaisonCompetition join = pigeonSaisonCompetitionRepository
                .findBySaisonPigeonIdAndCompetitionId(saisonPigeon.getId(), competition.getId())
                .orElseGet(() -> {
                    PigeonSaisonCompetition created = new PigeonSaisonCompetition();
                    created.setSaisonPigeon(saisonPigeon);
                    created.setCompetition(competition);
                    return pigeonSaisonCompetitionRepository.save(created);
                });

        Resultat resultat = resultatRepository.findByPigeonSaisonCompetition(join);
        if (resultat == null) {
            resultat = new Resultat();
            resultat.setPigeonSaisonCompetition(join);
        }
        resultat.setDistance((double) distanceKm);
        resultat.setVitesse(vitesse);
        resultat.setPoints(points);
        resultat.setClassement(classement);
        resultat.setDateArrivee(competition.getStartTime().plusMinutes(classement * 8L + 20L));
        resultatRepository.save(resultat);
    }

    private record SeedUser(String email, String rawPassword, String name, Role role) {
    }
}
