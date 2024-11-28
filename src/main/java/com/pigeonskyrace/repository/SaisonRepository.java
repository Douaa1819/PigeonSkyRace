package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Saison;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SaisonRepository extends MongoRepository<Saison, ObjectId> {
    Optional<Saison> findTopById(ObjectId id);
}
