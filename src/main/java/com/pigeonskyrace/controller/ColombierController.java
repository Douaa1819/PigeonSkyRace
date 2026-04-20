package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.response.ColombierResponseDTO;
import com.pigeonskyrace.dto.response.PigeonResponseDTO;
import com.pigeonskyrace.dto.request.ColombierRequestDTO;
import com.pigeonskyrace.mapper.ColombierMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.security.SecurityUtils;
import com.pigeonskyrace.security.UserPrincipal;
import com.pigeonskyrace.service.ColombierService;
import com.pigeonskyrace.service.PigeonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<ColombierResponseDTO> createColombier(
            @RequestBody @Valid ColombierRequestDTO colombierRequestDTO) {

        UserPrincipal principal = SecurityUtils.currentUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        ObjectId userId = new ObjectId(principal.getId());

        try {
            ColombierResponseDTO responseDTO = colombierService.save(colombierRequestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ColombierResponseDTO>> getAllColombiers() {
        UserPrincipal principal = SecurityUtils.currentUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        List<Colombier> colombiers = colombierService.findAllVisible(principal.getRole(), new ObjectId(principal.getId()));

        List<ColombierResponseDTO> colombierResponseDTOs = colombiers.stream()
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

                    ColombierResponseDTO dto = colombierMapper.toColombierResponseDTO(colombier);
                    dto.setPigeons(pigeonDTOs);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(colombierResponseDTOs);
    }
}
