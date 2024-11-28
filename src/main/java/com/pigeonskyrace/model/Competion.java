package com.pigeonskyrace.model;
import com.pigeonskyrace.utils.CompetitionId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "competitions")
    public class Competion {

    @MongoId
    private ObjectId id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Indexed(unique = true)
    private String nom;

    @NotNull(message = "La date de début est requise")
    private LocalDateTime startTime;

    @NotNull(message = "La date de fin est requise")

    private LocalDateTime endTime;

    @NotNull(message = "Les coordonnées GPS sont requises")
    private double latitudeGPS;

    @NotNull(message = "Les coordonnées GPS sont requises")
    private double longitudeGPS;

    @Min(value = 1, message = "Le nombre de pigeons doit être au moins de 1")
    private int nombrePigeons;

    private double pourcentageAdmission = 25.0;

    @DBRef
    private Saison saison;
}
