package com.pigeonskyrace.dto.request;

import com.pigeonskyrace.model.enums.Sexe;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PigeonRequestDTO {

    private String numeroBague;

    @NotBlank(message = "Le sexe est requis")
    private Sexe sexe;

    @NotNull(message = "L'âge est requis")
    private Integer age;

    @NotBlank(message = "La couleur est requise")
    @Size(max = 50)
    private String couleur;


    @NotNull(message = "L'ID du colombier est requis")
    private String colombierId;

    public @NotBlank(message = "Le sexe est requis") Sexe getSexe() {
        return sexe;
    }

    public void setSexe(@NotBlank(message = "Le sexe est requis") Sexe sexe) {
        this.sexe = sexe;
    }

    public @NotNull(message = "L'âge est requis") Integer getAge() {
        return age;
    }

    public void setAge(@NotNull(message = "L'âge est requis") Integer age) {
        this.age = age;
    }

    public @NotBlank(message = "La couleur est requise") @Size(max = 50) String getCouleur() {
        return couleur;
    }

    public void setCouleur(@NotBlank(message = "La couleur est requise") @Size(max = 50) String couleur) {
        this.couleur = couleur;
    }

    public @NotNull(message = "L'ID du colombier est requis") String getColombierId() {
        return colombierId;
    }

    public void setColombierId(@NotNull(message = "L'ID du colombier est requis") String colombierId) {
        this.colombierId = colombierId;
    }

    public String getNumeroBague() {
        return numeroBague;
    }

    public void setNumeroBague(String numeroBague) {
        this.numeroBague = numeroBague;
    }
}