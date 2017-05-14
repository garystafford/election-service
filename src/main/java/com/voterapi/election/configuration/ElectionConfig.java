package com.voterapi.election.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElectionConfig {

    /**
     * Used for eventually consistent example
     *
     * @return
     */
    @Bean
    public Queue candidateQueue() {
        return new Queue("elections.queue");
    }

//    @Bean
//    public FanoutExchange candidateFanoutExchange() {
//        return new FanoutExchange("election.fanout");
//    }
}