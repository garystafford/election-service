package com.voterapi.election.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.servicebus.IQueueClient;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.voterapi.election.domain.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ElectionEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Environment environment;

    @Autowired
    public ElectionEventHandler(Environment environment) {
        this.environment = environment;
    }

    @HandleAfterCreate
    public void handleElectionSave(Election elections) {
            sendMessageAzureServiceBus(elections);
    }

        private void sendMessageAzureServiceBus(Election election) {
            String connectionString = environment.getProperty("azure.service-bus.connection-string");
            String queueName = "elections.queue";

            try {
                IQueueClient queueSendClient = new QueueClient(
                        new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.PEEKLOCK);
                String message = serializeToJson(election);
                queueSendClient.sendAsync(new Message(message))
                        .thenRunAsync(queueSendClient::closeAsync);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ServiceBusException e) {
                e.printStackTrace();
            }
        }

    public String serializeToJson(Election elections) {
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
