package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.service.PigeonService;
import com.pigeonskyrace.service.SaisonPigeonService;
import com.pigeonskyrace.service.SaisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/saisonPigeons")
public class SaisonPigeonController {


    private final SaisonPigeonService saisonPigeonService;
    private final SaisonService saisonService;
    private final PigeonService pigeonService;

    @PostMapping("/{saisonId}/addPigeon")
    public SaisonPigeonResponseDTO addPigeonToSaison(
            @PathVariable String saisonId,
            @Valid @RequestBody SaisonPigeonRequestDTO requestDTO) {
        Saison saison = saisonService.findById(new ObjectId(saisonId));
        Pigeon pigeon = pigeonService.findById(new ObjectId(requestDTO.getPigeonId()));
        return saisonPigeonService.addPigeonToSaison(saison, pigeon);
    }

    @GetMapping("/{saisonId}/{pigeonId}")
    public ResponseEntity<SaisonPigeonResponseDTO> getSaisonPigeonBySaisonIdAndPigeonId(
            @PathVariable String saisonId,
            @PathVariable String pigeonId) {
        SaisonPigeonResponseDTO response = saisonPigeonService.getSaisonPigeonBySaisonIdAndPigeonId(saisonId, pigeonId);
        return ResponseEntity.ok(response);
    }

}
