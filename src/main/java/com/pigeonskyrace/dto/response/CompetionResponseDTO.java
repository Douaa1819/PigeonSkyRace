package com.pigeonskyrace.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompetionResponseDTO {
    private String id;
    private String nom;
    private double latitudeGPS;
    private double longitudeGPS;
    private String saisonId;
    private int nbPigeons;
    private double pourcentageAdmission = 25.0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
