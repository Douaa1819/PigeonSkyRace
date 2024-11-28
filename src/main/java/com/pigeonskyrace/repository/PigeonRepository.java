package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Pigeon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PigeonRepository  extends MongoRepository<Pigeon,ObjectId> {

    Optional<Pigeon>findByNumeroBague(String numeroBague);
    List<Pigeon> findByColombierId(ObjectId colombierId);
    Optional<Pigeon>findById(ObjectId id);
}
