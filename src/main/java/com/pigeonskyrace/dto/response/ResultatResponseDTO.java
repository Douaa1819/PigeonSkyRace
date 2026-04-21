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
    /** Final rank / classement for this competition (1 = first). */
    private Integer rank;
    private String pigeonId;
    /** Ring / band number — primary athlete identifier in the UI. */
    private String ringNumber;
    private String loftName;
    /** Optional portrait URL when set on the pigeon; clients may fall back to generated art. */
    private String imageUrl;
}
