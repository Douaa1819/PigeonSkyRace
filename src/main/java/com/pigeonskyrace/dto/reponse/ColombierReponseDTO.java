package com.pigeonskyrace.dto.reponse;

import lombok.Data;
import org.bson.types.ObjectId;


@Data
public class ColombierReponseDTO {
    private ObjectId id;
    private String nomColombier;
    private double coordonneeGPSlatitude;
    private double coordonneeGPSlongitude;
    private String proprietaireId;
}