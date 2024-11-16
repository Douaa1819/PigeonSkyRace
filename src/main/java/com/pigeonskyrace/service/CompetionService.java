package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.mapper.CompetionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.repository.CompetionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetionService {

    @Autowired

    private CompetionRepository competionRepository;
    private final CompetionMapper mapper;


    public Competion save(Competion competion) {

    return competionRepository.save(competion);

    }
    public List<Competion> findAll() {
        return competionRepository.findAll();
    }

    public Competion getCurrentCompetition() {
        LocalDateTime now = LocalDateTime.now();
        return competionRepository.findFirstByStartTimeBeforeAndEndTimeAfter(now,now).orElseThrow((
                () -> new RuntimeException("No competition found")
                ));
    }

    public CompetionReponseDTO getCompetitionid(String id) throws ChangeSetPersister.NotFoundException {
        Competion competition = competionRepository.findById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        return mapper.toDto(competition);
    }


}

