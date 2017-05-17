package com.voterapi.election.service;

import com.voterapi.election.domain.Election;
import com.voterapi.election.domain.ElectionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        String actual = "{\"id\":null,\"date\":1509840000000,\"electionType\":\"FEDERAL\",\"title\":\"2017 Test Election\"}";

        String expected = electionEventHandler.serializeToJson(election);

        Assert.assertEquals(expected, actual);
    }

}
