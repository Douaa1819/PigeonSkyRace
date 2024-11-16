package com.pigeonskyrace.dto.reponse;

import com.pigeonskyrace.model.enums.Sexe;
import lombok.Data;

@Data
public class PigeonResponseDTO {
    private String id;
    private String numeroBague;
    private Sexe sexe;
    private Integer age;
    private String couleur;
    private String colombierId;

}