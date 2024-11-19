package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.PigeonSaisonCompetitionResponseDTO;
import com.pigeonskyrace.dto.request.PigeonSaisonCompetitionRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.PigeonSaisonCompetitionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.SaisonPigeon;
import com.pigeonskyrace.repository.PigeonSaisonCompetitionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PigeonSaisonCompetitionService {

    @Autowired
    private PigeonSaisonCompetitionRepository repository;

    @Autowired
    private PigeonSaisonCompetitionMapper mapper;

    public PigeonSaisonCompetition registerPigeonInCompetition(PigeonSaisonCompetition pigeonSaisonCompetition) {
        // Convertir l'ID SaisonPigeon et Competition de String en ObjectId si nécessaire
        ObjectId saisonPigeonId = pigeonSaisonCompetition.getSaisonPigeon().getId();
        ObjectId competitionId = new ObjectId(pigeonSaisonCompetition.getCompetition().getId());

        // Vérifier si l'inscription existe déjà
        if (repository.existsBySaisonPigeonIdAndCompetitionId(saisonPigeonId, competitionId)) {
            throw new IllegalStateException("Ce pigeon est déjà inscrit dans cette compétition.");
        }

        // Sauvegarder l'inscription du pigeon dans la compétition
        PigeonSaisonCompetition savedEntity = repository.save(pigeonSaisonCompetition);

        // Retourner l'entité directement, pas un DTO
        return savedEntity;
    }






    public Optional<PigeonSaisonCompetition> findBySeasonPigeonAndCompetition(SaisonPigeon saisonPigeonId, Competion competitionId) {
        try {
            return repository.findBySaisonPigeonIdAndCompetitionId(saisonPigeonId, competitionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("No matching PigeonSaisonCompetition found for the given IDs.");
        }
    }




}

