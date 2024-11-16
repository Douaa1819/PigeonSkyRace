package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.SaisonPigeonResponseDTO;
import com.pigeonskyrace.dto.request.SaisonPigeonRequestDTO;
import com.pigeonskyrace.service.SaisonPigeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/saisonPigeons")
public class SaisonPigeonController {

    @Autowired
    private SaisonPigeonService saisonPigeonService;

    @PostMapping("/{saisonId}/addPigeon")
    public SaisonPigeonResponseDTO addPigeonToSaison(
            @PathVariable String saisonId,
            @RequestBody SaisonPigeonRequestDTO requestDTO) {
        return saisonPigeonService.addPigeonToSaison(saisonId, requestDTO);
    }

    @GetMapping("/{id}")
    public SaisonPigeonResponseDTO getSaisonPigeonById(@PathVariable String id) {
        return saisonPigeonService.getSaisonPigeonById(id);
    }

}
