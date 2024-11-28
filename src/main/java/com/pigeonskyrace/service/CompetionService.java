package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.CompetionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.repository.CompetionRepository;
import com.pigeonskyrace.utils.CompetitionId;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompetionService {

    private final CompetionRepository competionRepository;
    private final CompetionMapper mapper;


    public Competion save(Competion competion) {

        return competionRepository.save(competion);

    }
    public List<Competion> findAll() {
        return competionRepository.findAll();
    }


    public CompetionReponseDTO getCompetitionid(ObjectId competitionId) {

        return competionRepository.findById(competitionId)
                .map(mapper::toDto)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("Competition not found for ID: " + competitionId);
                });

    }

}
