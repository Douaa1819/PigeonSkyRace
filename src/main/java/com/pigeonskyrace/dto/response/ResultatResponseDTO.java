package com.pigeonskyrace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultatResponseDTO {
    private String id;
    private LocalDateTime dateArrivee;
    private Double distance;
    private Double vitesse;
    private Double points;
}
