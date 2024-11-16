package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.mapper.PigeonMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.ColombierService;
import com.pigeonskyrace.service.PigeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pigeons")
@RequiredArgsConstructor
public class PigeonController {
    private final PigeonService pigeonService;
    private final ColombierService colombierService;
    private final PigeonMapper pigeonMapper;


    @PostMapping
    public ResponseEntity<PigeonResponseDTO> createPigeon(@RequestBody PigeonRequestDTO pigeonRequestDTO) {

      //  Colombier colombier = colombierService.findByNomColombier(pigeonRequestDTO.getColombier())
                //.orElseThrow(() -> new RuntimeException("Colombier non trouvé : " + pigeonRequestDTO.getColombier()));

        Pigeon pigeon = pigeonMapper.toPigeon(pigeonRequestDTO);
       // pigeon.setColombier(colombier);

        Pigeon savedPigeon = pigeonService.save(pigeon);

        return ResponseEntity.ok(pigeonMapper.toPigeonResponseDTO(savedPigeon));
    }


    @GetMapping
    public ResponseEntity<List<PigeonResponseDTO>> getAllPigeons() {
        List<Pigeon> pigeons = pigeonService.findAll();
        List<PigeonResponseDTO> PigeonResponseDTOs = pigeons.stream()
                .map(pigeonMapper::toPigeonResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(PigeonResponseDTOs);
    }

}