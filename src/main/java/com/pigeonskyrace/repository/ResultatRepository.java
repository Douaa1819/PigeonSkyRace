package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Resultat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResultatRepository extends MongoRepository<Resultat, String> {
}
