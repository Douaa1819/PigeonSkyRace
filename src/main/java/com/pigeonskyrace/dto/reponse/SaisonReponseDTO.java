package com.pigeonskyrace.dto.reponse;

import com.pigeonskyrace.model.Competion;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaisonReponseDTO {
    private String id;
    private Date date;
    private String nom;
    private String description;
    private List<CompetionReponseDTO> competions;
}
