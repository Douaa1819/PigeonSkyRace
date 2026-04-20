package com.pigeonskyrace.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ColombierResponseDTO {
    private String id;
    private String nomColombier;
    private double coordonneeGPSlatitude;
    private double coordonneeGPSlongitude;
    private String userId;
    private List<PigeonResponseDTO> pigeons;
}
