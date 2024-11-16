package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PigeonSaisonCompetitionRepository extends MongoRepository<PigeonSaisonCompetition, ObjectId> {
    boolean existsBySaisonPigeonIdAndCompetitionId(ObjectId saisonPigeonId, ObjectId competitionId);
   Optional <PigeonSaisonCompetition> findPigeonSaisonCompetitionByCompetitionIdAndSaisonPigeonId(String competitionId, ObjectId saisonPigeonId);
}