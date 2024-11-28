package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Pigeon;
import com.pigeonskyrace.model.Saison;
import com.pigeonskyrace.model.SaisonPigeon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SaisonPigeonRepository extends MongoRepository<SaisonPigeon, String> {

    Optional<SaisonPigeon> findBySaisonIdAndPigeonId(ObjectId saisonId, ObjectId pigeonId);

}
