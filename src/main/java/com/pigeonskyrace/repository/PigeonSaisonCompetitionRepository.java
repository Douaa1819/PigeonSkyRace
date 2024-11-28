package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Competion;
import com.pigeonskyrace.model.PigeonSaisonCompetition;
import com.pigeonskyrace.model.SaisonPigeon;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PigeonSaisonCompetitionRepository extends MongoRepository<PigeonSaisonCompetition, ObjectId> {
    boolean existsBySaisonPigeonIdAndCompetitionId(ObjectId saisonPigeonId, ObjectId competitionId);
    @Query("{ 'saisonPigeon.$id' : ?0, 'competition.$id' : ?1 }")
    Optional<PigeonSaisonCompetition> findBySaisonPigeonIdAndCompetitionId(ObjectId saisonPigeonId, ObjectId competitionId);
   Optional< List<PigeonSaisonCompetition>>findAllByCompetition(Competion competion);
   Optional<List<PigeonSaisonCompetition>>findByCompetition_Id(ObjectId competitionId);
}