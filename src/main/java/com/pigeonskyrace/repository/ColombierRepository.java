package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Colombier;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ColombierRepository extends MongoRepository<Colombier, ObjectId> {
    Optional<Colombier>findById(ObjectId id);
}
