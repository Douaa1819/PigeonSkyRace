package com.pigeonskyrace.service;

import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.enums.Sexe;
import com.pigeonskyrace.repository.PigeonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PigeonService {
    @Autowired
    private PigeonRepository pigeonRepository;


    public Pigeon save(Pigeon pigeon) {
        if (pigeon.numeroBague() == null) {
            pigeon = pigeon.withNumeroBague(generateNumeroBague(pigeon.sexe(), pigeon.age())); // Mise à jour avec un numéro de bague
        }
        return pigeonRepository.save(pigeon);
    }


    public List<Pigeon> findAll() {
        return pigeonRepository.findAll();
    }


    public Pigeon findByNumeroBague(String numeroBague) {
        return pigeonRepository.findByNumeroBague(numeroBague)
                .orElseThrow(() -> new EntityNotFoundException("Pigeon avec numéro de bague " + numeroBague + " introuvable"));
    }

    // Génère un numéro de bague basé sur le sexe et l'âge
    private String generateNumeroBague(Sexe sexe, Integer age) {
        String prefix = (sexe == Sexe.FEMALE) ? "F" : "M";
        Random rand = new Random();
        int n = rand.nextInt(90) + 10; // Génère un nombre à deux chiffres
        // Calcul de l'année de naissance
        int birthYear = LocalDate.now().minusYears(age).getYear();
        String yearSuffix = String.valueOf(birthYear).substring(2); // Prend les deux derniers chiffres de l'année
        return prefix + "**" + n + "-" + yearSuffix;
    }
}