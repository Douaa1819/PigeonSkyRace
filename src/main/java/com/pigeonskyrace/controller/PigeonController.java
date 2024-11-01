package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.mapper.PigeonMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.ColombierService;
import com.pigeonskyrace.service.PigeonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pigeons")
public class PigeonController {
    private final PigeonService pigeonService;
    private final ColombierService colombierService;
    private final PigeonMapper pigeonMapper;

    public PigeonController(PigeonService pigeonService, ColombierService colombierService, PigeonMapper pigeonMapper) {
        this.pigeonService = pigeonService;
        this.colombierService = colombierService;
        this.pigeonMapper = pigeonMapper;
    }

    @PostMapping
    public ResponseEntity<PigeonResponseDTO> createPigeon(@RequestBody PigeonRequestDTO pigeonRequestDTO) {
        Colombier colombier = colombierService.findByNomColombier(pigeonRequestDTO.getColombier())
                .orElseThrow(() -> new RuntimeException("Colombier non trouvé : " + pigeonRequestDTO.getColombier()));

        Pigeon pigeon = pigeonMapper.toPigeon(pigeonRequestDTO);
        pigeon.setColombier(colombier); // Associer le pigeon au colombier

        Pigeon savedPigeon = pigeonService.save(pigeon);

        return ResponseEntity.ok(pigeonMapper.toPigeonResponseDTO(savedPigeon));
    }


}