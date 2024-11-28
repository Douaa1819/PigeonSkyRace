package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.SaisonReponseDTO;
import com.pigeonskyrace.dto.request.SaisonRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.SaisonMapper;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.repository.SaisonRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaisonService {
    @Autowired
    private SaisonRepository saisonRepository;
    @Autowired
    private final SaisonMapper saisonMapper;

    public SaisonService(SaisonMapper saisonMapper) {
        this.saisonMapper = saisonMapper;
    }

    public SaisonReponseDTO createSaison(SaisonRequestDTO saisonRequestDTO) {

        Saison saison = saisonMapper.toEntity(saisonRequestDTO);


        Saison savedSaison = saisonRepository.save(saison);

        return saisonMapper.toDto(savedSaison);
    }

    public Saison save(Saison saison) {
        return saisonRepository.save(saison);
    }

    public List<Saison> findAll() {
        return saisonRepository.findAll();
    }

    public Saison findById(ObjectId id) {
        return saisonRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("saison not found"));
    }
}
