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
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ElectionEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RabbitTemplate rabbitTemplate;
    private Queue electionCreatedQueue;
    private Queue electionDeletedQueue;
    private Queue electionUpdatedQueue;

    @Autowired
    public ElectionEventHandler(RabbitTemplate rabbitTemplate,
                                Queue electionCreatedQueue,
                                Queue electionUpdatedQueue,
                                Queue electionDeletedQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.electionCreatedQueue = electionCreatedQueue;
        this.electionUpdatedQueue = electionUpdatedQueue;
        this.electionDeletedQueue = electionDeletedQueue;
    }

    @HandleAfterCreate
    public void handleAfterCreated(Election election) {
        rabbitTemplate.convertAndSend(
                electionCreatedQueue.getName(), serializeToJson(election));
    }

    @HandleAfterSave
    public void handleAfterSaved(Election election) {
        rabbitTemplate.convertAndSend(
                electionUpdatedQueue.getName(), serializeToJson(election));
    }

    @HandleAfterDelete
    public void handleAfterDeleted(Election election) {
        rabbitTemplate.convertAndSend(
                electionDeletedQueue.getName(), serializeToJson(election));
    }

    public String serializeToJson(Election election) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "{}";

        try {
            jsonInString = mapper.writeValueAsString(election);
        } catch (JsonProcessingException e) {
            logger.error(String.valueOf(e));
        }

        logger.debug("Serialized message payload: {}", jsonInString);

        return jsonInString;
    }
}