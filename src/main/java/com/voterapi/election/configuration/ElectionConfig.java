package com.voterapi.election.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElectionConfig {

    @Bean
    public Queue electionQueue() {
        return new Queue("elections.queue");
    }
}
