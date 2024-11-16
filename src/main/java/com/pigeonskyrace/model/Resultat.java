package com.pigeonskyrace.model;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Resultat {

        @MongoId
        private ObjectId id;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dateArrivee;

        @Positive
        private Double distance;

        @Positive
        private Double vitesse;

        @Positive
        @Size(min = 1, max = 100)
        private Double points;

        @DBRef
        private PigeonSaisonCompetition pigeonSaisonCompetition;

        @CreatedDate
        private LocalDateTime dateCreation;


        public Resultat(ObjectId id, LocalDateTime dateArrivee, Double points, Double distance, Double vitesse, PigeonSaisonCompetition pigeonSaisonCompetition, LocalDateTime dateCreation) {
                this.id = id;
                this.dateArrivee = dateArrivee;
                this.points = points;
                this.distance = distance;
                this.vitesse = vitesse;
                this.pigeonSaisonCompetition = pigeonSaisonCompetition;
                this.dateCreation = dateCreation;
        }

        public ObjectId getId() {
                return id;
        }


        public LocalDateTime getDateArrivee() {
                return dateArrivee;
        }

        public void setDateArrivee(LocalDateTime dateArrivee) {
                this.dateArrivee = dateArrivee;
        }

        public Double getDistance() {
                return distance;
        }

        public void setDistance(Double distance) {
                this.distance = distance;
        }

        public Double getVitesse() {
                return vitesse;
        }

        public void setVitesse(Double vitesse) {
                this.vitesse = vitesse;
        }

        public Double getPoints() {
                return points;
        }

        public void setPoints(Double points) {
                this.points = points;
        }

        public PigeonSaisonCompetition getPigeonSaisonCompetition() {
                return pigeonSaisonCompetition;
        }

        public void setPigeonSaisonCompetition(PigeonSaisonCompetition pigeonSaisonCompetition) {
                this.pigeonSaisonCompetition = pigeonSaisonCompetition;
        }

        public LocalDateTime getDateCreation() {
                return dateCreation;
        }

        public void setDateCreation(LocalDateTime dateCreation) {
                this.dateCreation = dateCreation;
        }
}
