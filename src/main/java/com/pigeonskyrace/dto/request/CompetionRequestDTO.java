package com.pigeonskyrace.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
@Data
public class CompetionRequestDTO {
    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Indexed(unique = true)
    private String nom;


    @NotNull(message = "Les coordonnées GPS sont requises")
    private double latitudeGPS;

    @NotNull(message = "Les coordonnées GPS sont requises")
    private double longitudeGPS;

    @Min(value = 1, message = "Le nombre de pigeons doit être au moins de 1")
    private int nbPigeons;

    private String saisonId;
    private double pourcentageAdmission = 25.0;
    @NotNull(message = "La date de fin est requise")

    private LocalDateTime startTime;
    @NotNull(message = "La date de fin est requise")

    private LocalDateTime endTime;
}