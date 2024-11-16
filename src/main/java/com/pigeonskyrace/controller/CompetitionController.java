package com.pigeonskyrace.controller;

import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.request.CompetionRequestDTO;
import com.pigeonskyrace.mapper.CompetionMapper;
import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.service.CompetionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/competion")
public class CompetitionController {
    @Autowired
    private CompetionService competionService;
    @Autowired
    private CompetionMapper competionMapper;

    @PostMapping
    public ResponseEntity<CompetionReponseDTO>createCompetion(@RequestBody CompetionRequestDTO competionRequestDTO) {
        Competion competion= competionMapper.toEntity(competionRequestDTO);
        Competion competionSave=competionService.save(competion);
        CompetionReponseDTO response =competionMapper.toDto(competionSave);
        return ResponseEntity.ok(response);
    }

}
