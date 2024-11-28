package com.pigeonskyrace.service;

import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.reponse.UserResponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.mapper.ColombierMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.User;
import com.pigeonskyrace.repository.ColombierRepository;
import com.pigeonskyrace.repository.PigeonRepository;
import com.pigeonskyrace.repository.UserRepository;
import com.pigeonskyrace.utils.Coordinates;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColombierService {

    private final ColombierRepository colombierRepository;
    private final PigeonRepository pigeonRepository;
    private final ColombierMapper colombierMapper;
    private final UserRepository userRepository;

    public ColombierReponseDTO save(ColombierRequestDTO requestDTO, ObjectId userId) {
        String userIdString = userId.toHexString();
        User user = userRepository.findById(userIdString)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Colombier colombier = colombierMapper.toColombier(requestDTO, userId);


        colombier.setUser(user);  // Assigner l'objet User complet

        // Initialiser la liste des pigeons (vide au départ)
        colombier.setPigeons(new ArrayList<>());

        // Sauvegarder l'entité Colombier dans la base de données
        Colombier savedColombier = colombierRepository.save(colombier);

        // Retourner le DTO correspondant à l'entité Colombier sauvegardée
        return colombierMapper.toColombierResponseDTO(savedColombier);
    }




    public List<Colombier> findAll() {
        return colombierRepository.findAll();
    }

    public Optional<Colombier> findById(ObjectId id) {

        Colombier colombier = colombierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colombier non trouvé"));

        return Optional.of(colombier);
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
