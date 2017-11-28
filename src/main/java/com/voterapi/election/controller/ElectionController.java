package com.voterapi.election.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voterapi.election.domain.Election;
import com.voterapi.election.repository.ElectionRepository;
import com.voterapi.election.service.ElectionDemoListService;
import com.voterapi.election.service.ElectionEventHandler;
import com.voterapi.election.service.MessageBusUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.beans.EventHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElectionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ElectionRepository electionRepository;
    private ElectionDemoListService electionDemoListService;
    private MessageBusUtilities messageBusUtilities;

    @Autowired
    public ElectionController(ElectionRepository electionRepository,
                              ElectionDemoListService electionDemoListService,
                              MessageBusUtilities messageBusUtilities) {
        this.electionRepository = electionRepository;
        this.electionDemoListService = electionDemoListService;
        this.messageBusUtilities = messageBusUtilities;
    }

    /**
     * Serialize list of candidates to JSON
     *
     * @param elections
     * @return
     */
    public String serializeToJson(List<Election> elections) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        final Map<String, List<Election>> dataMap = new HashMap<>();
        dataMap.put("elections", elections);

        try {
            jsonInString = mapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            logger.info(String.valueOf(e));
        }

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

        for (Election election : elections) {
            messageBusUtilities.sendMessageAzureServiceBus(election);
        }

        Map<String, String> result = new HashMap<>();
        result.put("message", "Simulation data created!");

        if (logger.isDebugEnabled()) {
            logger.debug("Serialized message payload: {}", serializeToJson(elections));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result); // return 200 with payload
    }
}
