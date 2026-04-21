package com.pigeonskyrace.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PigeonResultHistoryItemDto {
    private String competitionId;
    private String competitionName;
    private LocalDateTime dateArrivee;
    private Double distance;
    private Double vitesse;
    private Double points;
    private Integer rank;
}
