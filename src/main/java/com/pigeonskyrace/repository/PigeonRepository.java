package com.pigeonskyrace.repository;

import com.pigeonskyrace.model.Pigeon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PigeonRepository  extends MongoRepository<Pigeon,String> {

    Optional<Pigeon>findByNumeroBague(String numeroBague);
}
