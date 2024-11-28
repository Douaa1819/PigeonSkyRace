package com.pigeonskyrace.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "resultat")
@Data
@Getter
@Setter
public class Resultat {

        @MongoId
        private ObjectId id;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dateArrivee;

        @Positive
        private Double distance;

        private Integer classement;

        @Positive
        private Double vitesse;

        @Positive
        @Size(min = 1, max = 100)
        private Double points;

        @DBRef
        private PigeonSaisonCompetition pigeonSaisonCompetition;

        @CreatedDate
        private LocalDateTime dateCreation;



}

