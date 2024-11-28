package com.pigeonskyrace.controller;


import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.request.CompetionRequestDTO;
import com.pigeonskyrace.mapper.CompetionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.service.CompetionService;
import com.pigeonskyrace.service.SaisonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/competions")
@RequiredArgsConstructor
public class CompetionController {

    @Autowired
    private final CompetionMapper competionMapper;
    private final CompetionService competionService;
    private  final SaisonService  saisonService;

    @PostMapping("")
    public ResponseEntity<CompetionReponseDTO> createCompetion(@RequestBody @Valid CompetionRequestDTO competionRequestDTO) {
        // Convertir CompetionRequestDTO en entité Competion
        Competion competion = competionMapper.toEntity(competionRequestDTO);

        // Rechercher la saison par son ID
        Saison saison = saisonService.findById(new ObjectId(competionRequestDTO.getSaisonId()));

        // Associer la compétition à la saison
        competion.setSaison(saison);

        // Sauvegarder la compétition
        Competion savedCompetion = competionService.save(competion);

        // Ajouter la compétition à la liste des compétitions de la saison
        saison.getCompetions().add(savedCompetion);

        // Sauvegarder la saison avec la compétition ajoutée
        saisonService.save(saison);

        // Convertir l'entité compétitions sauvegardée en DTO
        CompetionReponseDTO responseDto = competionMapper.toDto(savedCompetion);

        // Retourner la compétition créée avec un statut 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("")
    public ResponseEntity<List<CompetionReponseDTO>> getAllCompetions() {
        List<Competion> competions = competionService.findAll();
        List<CompetionReponseDTO> competionsDTO = competions.stream()
                .map(competionMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(competionsDTO);
    }
}



