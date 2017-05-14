package com.voterapi.election.repository;

import com.voterapi.election.domain.Election;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ElectionRepository extends MongoRepository<Election, String> {
        List<Election> findByElectionType(@Param("electionType") String electionType);
}
