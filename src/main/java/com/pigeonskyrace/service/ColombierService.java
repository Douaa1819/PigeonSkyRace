package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.ColombierMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.repository.ColombierRepository;
import com.pigeonskyrace.utils.Coordinates;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColombierService {

    private final ColombierRepository colombierRepository;
    private final ColombierMapper colombierMapper;

    public ColombierReponseDTO save(ColombierRequestDTO colombierRequestDTO, ObjectId userId) {
        Colombier colombier = colombierMapper.toColombier(colombierRequestDTO, userId);

        Colombier savedColombier = colombierRepository.save(colombier);

        return colombierMapper.toColombierResponseDTO(savedColombier);
    }




    public List<Colombier> findAll() {
        return colombierRepository.findAll();
    }
    /**
     * Récupère les coordonnées GPS du colombier associé à un ID.
     *
     * @param id L'identifiant du colombier.
     * @return Les coordonnées GPS sous forme d'objet `Coordinates`.
     */

    public Coordinates getLoftCoordinates(ObjectId id) {
        Colombier colombier = colombierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colombier avec l'ID " + id + " introuvable"));

        return new Coordinates(colombier.getCoordonneeGPSlatitude(), colombier.getCoordonneeGPSlongitude());
    }

}
