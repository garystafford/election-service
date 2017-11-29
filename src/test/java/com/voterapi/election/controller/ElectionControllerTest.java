package com.voterapi.election.controller;

import com.voterapi.election.domain.Election;
import com.voterapi.election.domain.ElectionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectionControllerTest {

    @Autowired
    private ElectionController electionController;

    private List<Election> elections = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Election election = new Election(
                new GregorianCalendar(2017, 10, 5).getTime(),
                ElectionType.FEDERAL,
                "2017 Test Election");
        elections.add(election);
    }

    @Test
    public void serializeToJson() throws Exception {
        String actual = "{\"elections\":[{\"id\":null,\"date\":1509840000000,\"electionType\":\"FEDERAL\",\"title\":\"2017 Test Election\"}]}";

        String expected = electionController.serializeToJson(elections);

        Assert.assertEquals(expected, actual);
    }
}
