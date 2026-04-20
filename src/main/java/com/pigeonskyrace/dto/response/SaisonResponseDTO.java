package com.pigeonskyrace.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaisonResponseDTO {
    private String id;
    private Date date;
    private String nom;
    private String description;
    private List<CompetionResponseDTO> competions;
}
