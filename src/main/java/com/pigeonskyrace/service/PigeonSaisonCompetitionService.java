package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.request.PigeonSaisonCompetitionRequestDTO;
import com.pigeonskyrace.dto.reponse.PigeonSaisonCompetitionResponseDTO;
import com.pigeonskyrace.mapper.PigeonSaisonCompetitionMapper;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.repository.PigeonSaisonCompetitionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class PigeonSaisonCompetitionService {

    @Autowired
    private PigeonSaisonCompetitionRepository repository;

    @Autowired
    private PigeonSaisonCompetitionMapper mapper;

    public PigeonSaisonCompetitionResponseDTO registerPigeonInCompetition(PigeonSaisonCompetitionRequestDTO requestDTO) {
        ObjectId saisonPigeonId = new ObjectId(requestDTO.getSaisonPigeonId());
        ObjectId competitionId = new ObjectId(requestDTO.getCompetitionId());

        if (repository.existsBySaisonPigeonIdAndCompetitionId(saisonPigeonId, competitionId)) {
            throw new IllegalStateException("Ce pigeon est déjà inscrit dans cette compétition.");
        }

        PigeonSaisonCompetition entity = mapper.toEntity(requestDTO);
        PigeonSaisonCompetition savedEntity = repository.save(entity);

        return mapper.toResponseDTO(savedEntity);
    }

    public PigeonSaisonCompetition findBySeasonPigeonAndCompetition(String competitionId, ObjectId saisonPigeonId) throws ChangeSetPersister.NotFoundException {
        return  repository.findPigeonSaisonCompetitionByCompetitionIdAndSaisonPigeonId(competitionId,saisonPigeonId).orElseThrow( () ->
                new ChangeSetPersister.NotFoundException()
        );
    }
}

