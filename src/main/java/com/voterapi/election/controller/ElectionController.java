package com.voterapi.election.controller;

import com.voterapi.election.domain.Election;
import com.voterapi.election.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ElectionController {

    private MongoTemplate mongoTemplate;
    private ElectionRepository electionRepository;

    @Autowired
    public ElectionController(MongoTemplate mongoTemplate,
                              ElectionRepository electionRepository) {
        this.mongoTemplate = mongoTemplate;
        this.electionRepository = electionRepository;
    }

    @RequestMapping(value = "/drop/elections", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAllElections() {
        electionRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<Election>>> getElectionSummary() {
        List<Election> elections = mongoTemplate.getCollection("election").distinct("title");
        return new ResponseEntity<>(Collections.singletonMap("elections", elections), HttpStatus.OK);
    }
}
