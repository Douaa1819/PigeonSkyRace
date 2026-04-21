package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.response.ResultatResponseDTO;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.Resultat;
import com.pigeonskyrace.model.SaisonPigeon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fills pigeon identity and loft fields on {@link ResultatResponseDTO} from the
 * {@link Resultat} → {@link PigeonSaisonCompetition} → {@link SaisonPigeon} → {@link Pigeon} graph.
 */
@Slf4j
@Component
public class ResultatResponseEnricher {

    public void enrich(Resultat resultat, ResultatResponseDTO dto) {
        if (resultat == null || dto == null) {
            return;
        }
        try {
            PigeonSaisonCompetition psc = resultat.getPigeonSaisonCompetition();
            if (psc == null) {
                return;
            }
            SaisonPigeon sp = psc.getSaisonPigeon();
            if (sp == null) {
                return;
            }
            Pigeon pigeon = sp.getPigeon();
            if (pigeon == null || pigeon.getId() == null) {
                return;
            }
            dto.setPigeonId(pigeon.getId().toHexString());
            dto.setRingNumber(pigeon.getNumeroBague());
            dto.setImageUrl(pigeon.getImageUrl());
            Colombier loft = pigeon.getColombier();
            if (loft != null) {
                dto.setLoftName(loft.getNomColombier());
            }
        } catch (Exception e) {
            log.debug("Could not enrich resultat {} with pigeon data: {}", resultat.getId(), e.getMessage());
        }
    }
}
