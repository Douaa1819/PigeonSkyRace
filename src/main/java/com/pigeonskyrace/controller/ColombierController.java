package com.pigeonskyrace.controller;
import com.pigeonskyrace.dto.reponse.ColombierReponseDTO;
import com.pigeonskyrace.dto.reponse.UserResponseDTO;
import com.pigeonskyrace.mapper.ColombierMapper;
import com.pigeonskyrace.model.Colombier;
import com.pigeonskyrace.service.ColombierService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("")
    public ResponseEntity<ColombierReponseDTO> createColombier(
            @RequestBody @Valid UserResponseDTO.ColombierRequestDTO colombierRequestDTO,
            HttpSession session) {

        // Vérification de l'utilisateur dans la session
        ObjectId userId = (ObjectId) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }


        ColombierReponseDTO responseDTO = colombierService.save(colombierRequestDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @GetMapping
    public ResponseEntity<List<ColombierReponseDTO>> getAllColombiers() {
        List<Colombier> colombiers = colombierService.findAll();
        List<ColombierReponseDTO> colombierResponseDTOs = colombiers.stream()
                .map(colombierMapper::toColombierResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(colombierResponseDTOs);

    }
}