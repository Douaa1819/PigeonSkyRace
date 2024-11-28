package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.PigeonSaisonCompetitionResponseDTO;
import com.pigeonskyrace.dto.request.PigeonSaisonCompetitionRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.PigeonSaisonCompetitionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.SaisonPigeon;
import com.pigeonskyrace.repository.PigeonSaisonCompetitionRepository;
import com.pigeonskyrace.utils.CompetitionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class PigeonSaisonCompetitionService {


    private final PigeonSaisonCompetitionRepository repository;
    private final PigeonSaisonCompetitionMapper mapper;

    public PigeonSaisonCompetitionResponseDTO registerPigeonInCompetition(PigeonSaisonCompetitionRequestDTO requestDTO) {

        PigeonSaisonCompetition pigeonSaisonCompetition = mapper.toEntity(requestDTO);
        ObjectId saisonPigeonId = pigeonSaisonCompetition.getSaisonPigeon().getId();
        ObjectId competitionId = pigeonSaisonCompetition.getCompetition().getId();


        if (repository.existsBySaisonPigeonIdAndCompetitionId(saisonPigeonId, competitionId)) {
            throw new IllegalStateException("Ce pigeon est déjà inscrit dans cette compétition.");
        }

        return  mapper.toResponseDTO(repository.save(pigeonSaisonCompetition));


    }

    public Optional<PigeonSaisonCompetition> findBySeasonPigeonAndCompetition(SaisonPigeon saisonPigeonId, Competion competion) {
        try {
            return repository.findBySaisonPigeonIdAndCompetitionId(saisonPigeonId.getId() , competion.getId());
        } catch (Exception e) {
            throw new EntityNotFoundException("No matching PigeonSaisonCompetition found for the given IDs.");
        }
    }

    public List<PigeonSaisonCompetition> findByCompetition(Competion competition) {
        log.info("Recherche des PigeonSaisonCompetitions pour la compétition : {}", competition);
        return repository.findAllByCompetition(competition) .orElseThrow(() -> new EntityNotFoundException("No pigeons found for the competition."));
    }


public List <PigeonSaisonCompetition> findByCompetitionId(ObjectId id){
        return  repository.findByCompetition_Id(id).orElseThrow(()-> new EntityNotFoundException("pigeonSaisonCompetition id not found"));
}
}

