package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.mapper.SaisonPigeonMapper;
import com.pigeonskyrace.model.SaisonPigeon;
import com.pigeonskyrace.repository.SaisonPigeonRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaisonPigeonService {

    @Autowired
    private SaisonPigeonRepository saisonPigeonRepository;

    @Autowired
    private SaisonPigeonMapper saisonPigeonMapper;

    public SaisonPigeonResponseDTO addPigeonToSaison(String saisonId, SaisonPigeonRequestDTO requestDTO) {
        SaisonPigeon saisonPigeon = saisonPigeonMapper.toEntity(requestDTO, saisonId);
        SaisonPigeon savedSaisonPigeon = saisonPigeonRepository.save(saisonPigeon);
        return saisonPigeonMapper.toDto(savedSaisonPigeon);
    }

    // Méthode pour obtenir une association spécifique par ID
    public SaisonPigeonResponseDTO getSaisonPigeonById(String id) {
        SaisonPigeon saisonPigeon = saisonPigeonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SaisonPigeon not found"));
        return saisonPigeonMapper.toDto(saisonPigeon);
    }

    public SaisonPigeon getSaisonPigeonBySaisonIdAndPigeonId(ObjectId saisonId, ObjectId pigeonId) {
        return saisonPigeonRepository.findSaisonPigeonBySaisonIdAndPigeonId(saisonId, pigeonId)
                .orElseThrow(() -> new RuntimeException("SaisonPigeon not found"));
    }





}
