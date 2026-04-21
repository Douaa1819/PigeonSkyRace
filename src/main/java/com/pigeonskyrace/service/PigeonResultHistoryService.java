package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.response.PigeonResultHistoryItemDto;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.Resultat;
import com.pigeonskyrace.repository.PigeonRepository;
import com.pigeonskyrace.repository.PigeonSaisonCompetitionRepository;
import com.pigeonskyrace.repository.ResultatRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PigeonResultHistoryService {

    private final PigeonRepository pigeonRepository;
    private final PigeonSaisonCompetitionRepository pigeonSaisonCompetitionRepository;
    private final ResultatRepository resultatRepository;

    public List<PigeonResultHistoryItemDto> historyForPigeon(String pigeonIdHex) {
        ObjectId pigeonId = new ObjectId(pigeonIdHex);
        pigeonRepository.findById(pigeonId).orElseThrow(() -> new EntityNotFoundException("Pigeon not found"));

        List<PigeonSaisonCompetition> participations = pigeonSaisonCompetitionRepository
                .findBySaisonPigeon_Pigeon_Id(pigeonId);

        List<PigeonResultHistoryItemDto> rows = new ArrayList<>();
        for (PigeonSaisonCompetition psc : participations) {
            Resultat r = resultatRepository.findByPigeonSaisonCompetition(psc);
            if (r == null) {
                continue;
            }
            Competion c = psc.getCompetition();
            if (c == null || c.getId() == null) {
                continue;
            }
            PigeonResultHistoryItemDto row = new PigeonResultHistoryItemDto();
            row.setCompetitionId(c.getId().toHexString());
            row.setCompetitionName(c.getNom());
            row.setDateArrivee(r.getDateArrivee());
            row.setDistance(r.getDistance());
            row.setVitesse(r.getVitesse());
            row.setPoints(r.getPoints());
            row.setRank(r.getClassement());
            rows.add(row);
        }

        rows.sort(Comparator.comparing(
                PigeonResultHistoryItemDto::getDateArrivee,
                Comparator.nullsLast(Comparator.naturalOrder())));

        return rows;
    }
}
