
    package com.pigeonskyrace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

    @Data
    public class ColombierRequestDTO {

        @NotBlank(message = "Le nom du colombier est requis")
        @Size(max = 50, message = "Le nom du colombier ne doit pas dépasser 50 caractères")
        private String nomColombier;

        private double coordonneeGPSlatitude;
        private double coordonneeGPSlongitude;
}
