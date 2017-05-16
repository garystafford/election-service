package com.voterapi.election.service;

import com.voterapi.election.domain.Election;
import com.voterapi.election.domain.ElectionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectionEventHandlerTest {

    @Autowired
    ElectionEventHandler electionEventHandler;

    Election election = new Election();

    @Before
    public void setUp() throws Exception {
        election = new Election(
                new GregorianCalendar(2017, 10, 5).getTime(),
                ElectionType.FEDERAL,
                "2017 Test Election");
    }

    @Test
    public void serializeToJson() throws Exception {
        String actual = "{\"id\":null,\"date\":1509854400000,\"electionType\":\"FEDERAL\",\"title\":\"2017 Test Election\"}";

        String expected = electionEventHandler.serializeToJson(election);

        Assert.assertEquals(expected, actual);
    }

}