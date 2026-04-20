package com.pigeonskyrace.controller;


import com.pigeonskyrace.dto.response.SaisonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonRequestDTO;
import com.pigeonskyrace.mapper.SaisonMapper;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.service.CompetionService;
import com.pigeonskyrace.service.SaisonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/saisons")
@RequiredArgsConstructor
public class SaisonController {

    private final SaisonService saisonService;
    private final SaisonMapper saisonMapper;


    @PostMapping("")
    public ResponseEntity<SaisonResponseDTO> createSaison(@RequestBody @Valid SaisonRequestDTO saisonRequestDTO) {
        SaisonResponseDTO responseDTO = saisonService.createSaison(saisonRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("")
    public ResponseEntity<List<SaisonResponseDTO>> getAllSaisons() {
        List<Saison> saisons = saisonService.findAll();
        List<SaisonResponseDTO> responseDTOs = saisons.stream()
                .map(saisonMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

}