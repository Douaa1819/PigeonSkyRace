package com.pigeonskyrace.dto.reponse;

import com.pigeonskyrace.model.PigeonSaisonCompetition;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ResultatReponseDTO {
       private String id;
        private LocalDateTime dateArrivee;

        private Double distance;

        private Double vitesse;

        private Double points;


}
