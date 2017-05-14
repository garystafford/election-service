package com.voterapi.election.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voterapi.election.domain.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ElectionEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RabbitTemplate rabbitTemplate;
    private Queue electionQueue;

    @Autowired
    public ElectionEventHandler(RabbitTemplate rabbitTemplate, Queue electionQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.electionQueue = electionQueue;
    }

    @HandleAfterCreate
    public void handleElectionSave(Election elections) {
        sendMessage(elections);
    }

    private void sendMessage(Election elections) {
        rabbitTemplate.convertAndSend(
                electionQueue.getName(), serializeToJson(elections));
    }

    private String serializeToJson(Election elections) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(elections);
        } catch (JsonProcessingException e) {
            logger.info(String.valueOf(e));
        }

        logger.debug("Serialized message payload: {}", jsonInString);

        return jsonInString;
    }
}
