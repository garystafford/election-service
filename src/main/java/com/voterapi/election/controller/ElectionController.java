package com.voterapi.election.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voterapi.election.domain.Election;
import com.voterapi.election.repository.ElectionRepository;
import com.voterapi.election.service.MessageBusUtilities;
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
    private MessageBusUtilities messageBusUtilities;

    @Autowired
    public ElectionController(ElectionRepository electionRepository,
                              MessageBusUtilities messageBusUtilities) {
        this.electionRepository = electionRepository;
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

    @RequestMapping(value = "/elections/drop", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteAllElections() {
        electionRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
