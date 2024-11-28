package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.SaisonPigeonMapper;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.model.SaisonPigeon;
import com.pigeonskyrace.repository.SaisonPigeonRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaisonPigeonService {

    private final SaisonPigeonRepository saisonPigeonRepository;
    private final SaisonPigeonMapper saisonPigeonMapper;

    public SaisonPigeonResponseDTO addPigeonToSaison(Saison saison, Pigeon pigeon) {
        saisonPigeonRepository.findBySaisonIdAndPigeonId(saison.getId(),pigeon.getId()).ifPresent(
                existingPigeon -> {
                    throw new EntityNotFoundException("this pigeon already exists");
                }
        );
        SaisonPigeon saisonPigeon= new SaisonPigeon();
        saisonPigeon.setSaison(saison);
        saisonPigeon.setPigeon(pigeon);
        SaisonPigeon savedSaisonPigeon = saisonPigeonRepository.save(saisonPigeon);

        return saisonPigeonMapper.toDto(savedSaisonPigeon);
    }

    public SaisonPigeonResponseDTO getSaisonPigeonById(String id) {
        SaisonPigeon saisonPigeon = saisonPigeonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SaisonPigeon not found with ID: " + id));
        return saisonPigeonMapper.toDto(saisonPigeon);
    }

    public SaisonPigeonResponseDTO getSaisonPigeonBySaisonIdAndPigeonId(String saisonId, String pigeonId) {
        ObjectId saisonObjectId = new ObjectId(saisonId);
        ObjectId pigeonObjectId = new ObjectId(pigeonId);

        System.out.println("Recherche SaisonPigeon avec SaisonId: " + saisonObjectId + " et PigeonId: " + pigeonObjectId);

        SaisonPigeon saisonPigeon = saisonPigeonRepository.findBySaisonIdAndPigeonId(saisonObjectId, pigeonObjectId)
                .orElseThrow(() -> {
                    String errorMessage = "SaisonPigeon not found for SaisonId: " + saisonId + " and PigeonId: " + pigeonId;
                    System.out.println(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });

        return saisonPigeonMapper.toDto(saisonPigeon);
    }






}
