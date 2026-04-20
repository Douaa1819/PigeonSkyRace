package com.pigeonskyrace.controller;

import com.lowagie.text.DocumentException;
import com.pigeonskyrace.dto.response.CompetionResponseDTO;
import com.pigeonskyrace.exception.EntityNotFoundException;
import com.pigeonskyrace.model.Resultat;
import com.pigeonskyrace.service.CompetionService;
import com.pigeonskyrace.service.PdfGenerationService;
import com.pigeonskyrace.service.ResultatService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PdfController {

    private final CompetionService competionService;
    private final PdfGenerationService pdfGenerationService;
    private final ResultatService resultatService;

    @GetMapping("/api/v1/resultats/{competitionId}/pdf")
    public ResponseEntity<byte[]> generateCompetitionPdf(@PathVariable String competitionId) throws DocumentException, IOException {
        ObjectId competition = new ObjectId(competitionId);
        CompetionResponseDTO competionResult = competionService.getCompetitionid(competition);

        List<Resultat> resultats = resultatService.getResultatsByCompetitionId(competition);
        if (resultats.isEmpty()) {
            throw new EntityNotFoundException("Aucun résultat trouvé pour la compétition avec l'ID : " + competitionId);
        }

        byte[] pdfBytes = pdfGenerationService.generateCompetitionResultPdf(competionResult, resultats);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=competition_results.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
