package com.pigeonskyrace.dto.response;

import com.pigeonskyrace.model.enums.Sexe;
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
    private String imageUrl;

    public PigeonResponseDTO(String id, String numeroBague, Sexe sexe, Integer age, String couleur) {
        this.id = id;
        this.numeroBague = numeroBague;
        this.sexe = sexe;
        this.age = age;
        this.couleur = couleur;
    }
}
