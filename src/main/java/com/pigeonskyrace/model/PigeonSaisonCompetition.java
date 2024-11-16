package com.pigeonskyrace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pigeonSaisonCompetitions")
public class PigeonSaisonCompetition {

    @MongoId
    private ObjectId id;

    private ObjectId saisonPigeonId;

    private ObjectId competitionId;

}
