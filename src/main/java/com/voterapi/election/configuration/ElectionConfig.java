package com.voterapi.election.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElectionConfig {

    @Bean
    public Queue electionCreatedQueue() {
        return new Queue("election_created_queue");
    }

    @Bean
    public Queue electionUpdatedQueue() {
        return new Queue("election_updated_queue");
    }

    @Bean
    public Queue electionDeletedQueue() {
        return new Queue("election_deleted_queue");
    }
}
