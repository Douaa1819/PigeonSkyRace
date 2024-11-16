package com.pigeonskyrace.dto.request;

import com.pigeonskyrace.model.PigeonSaisonCompetition;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ResultatRequestDTO (

        String numeroBague,
    LocalDateTime dateArrivée


){}