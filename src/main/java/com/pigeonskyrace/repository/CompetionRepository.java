package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Competion;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CompetionRepository extends MongoRepository<Competion, String> {
    Optional<Competion> findFirstByStartTimeBeforeAndEndTimeAfter(LocalDateTime dateDébut, LocalDateTime dateFin);
    Optional<Competion>findById(String id);
}
