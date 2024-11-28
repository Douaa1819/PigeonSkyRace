package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Competion;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface CompetionRepository extends MongoRepository<Competion, ObjectId> {
    Optional<Competion> findById(ObjectId id);
}
