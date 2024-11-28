package com.pigeonskyrace.controller;
import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.mapper.ColombierMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.ColombierService;
import com.pigeonskyrace.service.PigeonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/colombiers")
@RequiredArgsConstructor
public class ColombierController {

    private final ColombierService colombierService;
    private final ColombierMapper colombierMapper;
    private final PigeonService pigeonService;

    @PostMapping("")
    public ResponseEntity<ColombierReponseDTO> createColombier(
            @RequestBody @Valid com.pigeonskyrace.dto.request.ColombierRequestDTO colombierRequestDTO,
            HttpSession session) {

        // Récupérer l'ID de l'utilisateur à partir de la session
        ObjectId userId = (ObjectId) session.getAttribute("userId");

        // Vérifier si l'utilisateur est authentifié
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Retourne 401 Unauthorized si l'utilisateur n'est pas authentifié
        }

        // Utiliser le service pour créer le colombier avec l'ID du propriétaire
        try {
            // Sauvegarder le colombier avec l'ID de l'utilisateur
            ColombierReponseDTO responseDTO = colombierService.save(colombierRequestDTO, userId);

            // Retourner une réponse avec statut 201 (Created) et le DTO du colombier créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // En cas d'erreur, renvoyer un statut 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping("")
    public ResponseEntity<List<ColombierReponseDTO>> getAllColombiers() {
        List<Colombier> colombiers = colombierService.findAll();

        // Mapper les colombiers en DTO et ajouter les pigeons associés
        List<ColombierReponseDTO> colombierResponseDTOs = colombiers.stream()
                .map(colombier -> {
                    List<Pigeon> pigeons = pigeonService.findByColombierId(colombier.getId());
                    List<PigeonResponseDTO> pigeonDTOs = pigeons.stream()
                            .map(pigeon -> new PigeonResponseDTO(
                                    pigeon.getId().toHexString(),
                                    pigeon.getNumeroBague(),
                                    pigeon.getSexe(),
                                    pigeon.getAge(),
                                    pigeon.getCouleur()))
                            .collect(Collectors.toList());


                    ColombierReponseDTO dto = colombierMapper.toColombierResponseDTO(colombier);
                    dto.setPigeons(pigeonDTOs);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(colombierResponseDTOs);
    }
}