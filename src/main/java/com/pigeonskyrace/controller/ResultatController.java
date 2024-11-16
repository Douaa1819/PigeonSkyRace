package com.pigeonskyrace.controller;


import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.service.CompetionService;
import com.pigeonskyrace.service.ResultatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/resultats")
    public class ResultatController {

        private final ResultatService resultatService;
        private final CompetionService competionService;

        // ca pour créer un résultat pour une compétition spécifique
        @PostMapping("/{competitionId}")
        public ResponseEntity<ResultatReponseDTO> createResultat(
                @PathVariable String competitionId, // Récupération de l'ID de la compétition depuis l'URL
                @Valid @RequestBody ResultatRequestDTO resultRequestDto) throws ChangeSetPersister.NotFoundException {

            // Récupération des informations de la compétition à partir de son ID
            CompetionReponseDTO competitionResponseDto = competionService.getCompetitionid(competitionId);

            // Création du résultat en utilisant le service "resultatService" avec les informations de la compétition
            ResultatReponseDTO responseDto = resultatService.createResult(resultRequestDto, competitionResponseDto);
            String successMessage = "Les informations stockées avec succès pour la compétition : " ;


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseDto);
        }
    }


