package com.pigeonskyrace.controller;


import com.pigeonskyrace.dto.reponse.CompetionReponseDTO;
import com.pigeonskyrace.dto.reponse.ResultatReponseDTO;
import com.pigeonskyrace.dto.request.ResultatRequestDTO;
import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.service.CompetionService;
import com.pigeonskyrace.service.PigeonService;
import com.pigeonskyrace.service.ResultatService;
import com.pigeonskyrace.utils.CompetitionId;
import com.pigeonskyrace.utils.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/resultats")
public class ResultatController {

    private final ResultatService resultatService;
    private final CompetionService competionService;
    private final PigeonService pigeonService;


    @PostMapping("/{competitionId}")
    public ResponseEntity<ResponseApi<ResultatReponseDTO>> createResult(
            @PathVariable String competitionId,
            @RequestBody @Valid ResultatRequestDTO requestDto) {

        try {

            CompetionReponseDTO competitionDto = competionService.getCompetitionid(new ObjectId(competitionId));
            log.info("Competition details retrieved: {}", competitionDto);

            log.info("Fetching pigeon details for bag number: {}", requestDto.numeroBague());
            Pigeon pigeon = pigeonService.findByNumeroBague(requestDto.numeroBague());

            log.info("Pigeon found: {}", pigeon);

            ResultatReponseDTO responseDto = resultatService.createResult(requestDto, competitionDto);

            ResponseApi<ResultatReponseDTO> response = new ResponseApi<>(responseDto,
                    "Result successfully created for competition " + competitionId, HttpStatus.CREATED);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while creating result for competition ID: {}: {}", competitionId, e.getMessage(), e);
            return new ResponseEntity<>(new ResponseApi<>(null, "Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{competitionId}")
    public ResponseEntity<List<ResultatReponseDTO>> calculateResults(@PathVariable String competitionId) {
        CompetionReponseDTO competitionDto = competionService.getCompetitionid(new ObjectId(competitionId));
        log.info("Competition details retrieved: {}", competitionDto);
        List<ResultatReponseDTO> results = resultatService.calculatePoint(competitionDto);

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

}


