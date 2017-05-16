package com.voterapi.election.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectionConfigTest {

    @Autowired
    private ElectionConfig electionConfig;

    @Test
    public void testElectionQueue() throws Exception {
        Queue expected = electionConfig.electionQueue();
        Queue actual = new Queue("elections.queue");

        Assert.assertEquals(expected.getName(), actual.getName());
    }
}