package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.PigeonRequestDTO;
import com.pigeonskyrace.mapper.PigeonMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.ColombierService;
import com.pigeonskyrace.service.PigeonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pigeons")
@RequiredArgsConstructor

public class PigeonController {
    private final PigeonService pigeonService;
    private final PigeonMapper pigeonMapper;


    @PostMapping("")
    public ResponseEntity<PigeonResponseDTO> createPigeon(@RequestBody @Valid PigeonRequestDTO pigeonRequestDTO) {
        PigeonResponseDTO responseDTO = pigeonService.createPigeon(pigeonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
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