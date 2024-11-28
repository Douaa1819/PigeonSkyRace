package com.pigeonskyrace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "saisonPigeons")
public class SaisonPigeon {

    @MongoId
    private ObjectId id;

    @DBRef
    private Saison saison;

    @DBRef
    private Pigeon pigeon;
}
