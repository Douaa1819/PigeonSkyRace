package com.pigeonskyrace.dto.reponse;

import com.pigeonskyrace.model.enums.Sexe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PigeonResponseDTO {
    private String id;
    private String numeroBague;
    private Sexe sexe;
    private Integer age;
    private String couleur;
    private String colombierId;

    public PigeonResponseDTO(String id, String numeroBague, Sexe sexe, Integer age, String couleur) {
        this.id = id;
        this.numeroBague = numeroBague;
        this.sexe = sexe;
        this.age = age;
        this.couleur = couleur;
    }

}