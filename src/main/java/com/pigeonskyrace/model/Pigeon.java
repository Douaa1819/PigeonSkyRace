package com.pigeonskyrace.model;

import com.pigeonskyrace.model.enums.Sexe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Document(collection = "pigeons")
public record Pigeon(
        @MongoId ObjectId id,

        @Indexed(unique = true)
        @NotBlank(message = "Le numéro de bague est requis")
        @Size(max = 50)
        String numeroBague,

        @Indexed
        @NotBlank(message = "Le sexe est requis")
        Sexe sexe,

        @NotBlank(message = "L'âge est requis")
        Integer age,

        @NotBlank(message = "La couleur est requise")
        @Size(max = 50)
        String couleur,



        @DBRef
        Colombier colombier
) {
    public Pigeon(Sexe sexe, Integer age, String couleur, Colombier colombier) {
        this(
                null,
                generateNumeroBague(sexe, age),
                sexe,
                age,
                couleur,
                colombier
        );
    }


    private static String generateNumeroBague(Sexe sexe, Integer age) {
        String prefix = (sexe == Sexe.FEMALE) ? "F" : "M";
        Random rand = new Random();
        int n = rand.nextInt(90) + 10; // Génère un nombre à deux chiffres

        // Calcul de l'année de naissance
        int birthYear = LocalDate.now().minusYears(age).getYear();
        String yearSuffix = String.valueOf(birthYear).substring(2); // Prend les deux derniers chiffres de l'année

        return prefix + "**" + n + "-" + yearSuffix;
    }

    public Pigeon withNumeroBague(String newNumeroBague) {
        return new Pigeon(this.id, newNumeroBague, this.sexe, this.age, this.couleur, this.colombier);
    }
}
