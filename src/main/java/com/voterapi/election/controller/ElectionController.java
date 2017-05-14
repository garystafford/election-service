package com.voterapi.election.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voterapi.election.domain.Election;
import com.voterapi.election.repository.ElectionRepository;
import com.voterapi.election.service.ElectionDemoListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElectionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ElectionRepository electionRepository;
    private ElectionDemoListService electionDemoListService;

    @Autowired
    public ElectionController(ElectionRepository electionRepository,
                              ElectionDemoListService electionDemoListService) {
        this.electionRepository = electionRepository;
        this.electionDemoListService = electionDemoListService;
    }

    /**
     * Serialize list of candidates to JSON
     *
     * @param elections
     * @return
     */
    private String serializeToJson(List<Election> elections) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        final Map<String, List<Election>> dataMap = new HashMap<>();
        dataMap.put("elections", elections);

        try {
            jsonInString = mapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            logger.info(String.valueOf(e));
        }

//        logger.debug("Serialized message payload: {}", jsonInString);

        return jsonInString;
    }

    /**
     * Populates database with list of candidates
     *
     * @return
     */
    @RequestMapping(value = "/simulation", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getSimulation() {
        electionRepository.deleteAll();
        List<Election> elections = electionDemoListService.getElections();
        electionRepository.save(elections);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Simulation data created!");
        logger.debug("Serialized message payload: {}", serializeToJson(elections));

        return ResponseEntity.status(HttpStatus.OK).body(result); // return 200 with payload
    }
}
