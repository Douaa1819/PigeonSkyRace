package com.pigeonskyrace.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class PigeonSaisonCompetitionRequestDTO {

    @NotBlank(message = "saisonPigeonId is required")
    private String saisonPigeonId;

    private String competitionId;
}
