package com.pigeonskyrace.dto.reponse;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;


@Data
public class ColombierReponseDTO {
    private String id;
    private String nomColombier;
    private double coordonneeGPSlatitude;
    private double coordonneeGPSlongitude;
    private String userId;
    private List<PigeonResponseDTO> pigeons;
}