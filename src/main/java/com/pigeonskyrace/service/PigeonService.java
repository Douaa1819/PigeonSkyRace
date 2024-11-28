package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.PigeonMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.enums.Sexe;
import com.pigeonskyrace.repository.PigeonRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
@RequiredArgsConstructor
@Service
public class PigeonService {

    private final PigeonRepository pigeonRepository;
    private final ColombierService colombierService;
    private final PigeonMapper pigeonMapper;

    public PigeonResponseDTO createPigeon(PigeonRequestDTO pigeonRequestDTO) {
        // Récupérer le colombier via l'ID
        Colombier colombier = colombierService.findById(new ObjectId(pigeonRequestDTO.getColombierId()))
                .orElseThrow(() -> new EntityNotFoundException("Colombier non trouvé pour l'ID : " + pigeonRequestDTO.getColombierId()));

        // Si le numeroBague est null ou vide, générer un numeroBague
        if (pigeonRequestDTO.getNumeroBague() == null || pigeonRequestDTO.getNumeroBague().isEmpty()) {
            pigeonRequestDTO.setNumeroBague(generateNumeroBague(pigeonRequestDTO.getSexe(), pigeonRequestDTO.getAge()));
        }

        // Mapper PigeonRequestDTO en Pigeon avec le colombier trouvé
        Pigeon pigeon = pigeonMapper.toPigeon(pigeonRequestDTO).withColombier(colombier);

        // Sauvegarder le pigeon dans la base de données
        Pigeon savedPigeon = pigeonRepository.save(pigeon);

        // Retourner le DTO du Pigeon
        return pigeonMapper.toPigeonResponseDTO(savedPigeon);
    }

    public List<Pigeon> findByColombierId(ObjectId colombierId) {
        return pigeonRepository.findByColombierId(colombierId);
    }


    public List<Pigeon> findAll() {
        return pigeonRepository.findAll();
    }


    public Pigeon findByNumeroBague(String numeroBague) {
        return pigeonRepository.findByNumeroBague(numeroBague)
                .orElseThrow(() -> new EntityNotFoundException("Pigeon avec numéro de bague " + numeroBague + " introuvable"));
    }

    public Pigeon findById(ObjectId id) {

        return pigeonRepository.findById(id).orElseThrow(()->new EntityNotFoundException("pigeon not found"));
    }

    // Génère un numéro de bague basé sur le sexe et l'âge
    private String generateNumeroBague(Sexe sexe, Integer age) {
        String prefix = (sexe == Sexe.FEMALE) ? "F" : "M";
        Random rand = new Random();
        int n = rand.nextInt(90) + 10;
        // Calcul de l'année de naissance
        int birthYear = LocalDate.now().minusYears(age).getYear();
        String yearSuffix = String.valueOf(birthYear).substring(2);
        return prefix + "**" + n + "-" + yearSuffix;
    }
}