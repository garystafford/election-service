package com.voterapi.election.controller;

import com.voterapi.election.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElectionController {

    private ElectionRepository electionRepository;

    @Autowired
    public ElectionController(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    @RequestMapping(value = "/elections/drop", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAllElections() {
        electionRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
