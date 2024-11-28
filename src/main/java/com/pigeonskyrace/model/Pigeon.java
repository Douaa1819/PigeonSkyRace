package com.pigeonskyrace.model;

import com.pigeonskyrace.model.enums.Sexe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Data
@NoArgsConstructor
@Document(collection = "pigeons")
public class Pigeon {

    @MongoId
    private ObjectId id;

    @Indexed(unique = true)
    @NotBlank(message = "Le numéro de bague est requis")
    @Size(max = 50)
    private String numeroBague;

    @Indexed
    @NotBlank(message = "Le sexe est requis")
    private Sexe sexe;

    @NotBlank(message = "L'âge est requis")
    private Integer age;

    @NotBlank(message = "La couleur est requise")
    @Size(max = 50)
    private String couleur;

    private LocalDate dateNaissance;

    // Référence au colombier auquel ce pigeon appartient
    @DBRef
    private Colombier colombier;

    public Pigeon(Sexe sexe, Integer age, String couleur, LocalDate dateNaissance, Colombier colombier) {
        this.sexe = sexe;
        this.age = age;
        this.couleur = couleur;
        this.dateNaissance = dateNaissance;
        this.colombier = colombier;
        this.numeroBague = generateNumeroBague(sexe, dateNaissance);
    }

    private String generateNumeroBague(Sexe sexe, LocalDate dateNaissance) {
        String prefix = (sexe == Sexe.FEMALE) ? "F" : "M";
        Random rand = new Random();
        int n = rand.nextInt(900) + 100;
        String yearSuffix = dateNaissance.format(DateTimeFormatter.ofPattern("yy"));
        return prefix + "***" + n + "-" + yearSuffix;
    }

    public Pigeon withColombier(Colombier colombier) {
        this.colombier = colombier;
        return this;
    }
}