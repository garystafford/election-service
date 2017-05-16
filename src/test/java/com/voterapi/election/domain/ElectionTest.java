package com.voterapi.election.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectionTest {

    Election election = new Election();

    @Before
    public void setUp() throws Exception {
        election = new Election(
                new GregorianCalendar(2017, 10, 5).getTime(),
                ElectionType.FEDERAL,
                "2017 Test Election");
    }

    @Test
    public void testGetDate() throws Exception {
        Date actual = new GregorianCalendar(2017, 10, 5).getTime();
        Date expected = election.getDate();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetElectionType() throws Exception {
        ElectionType actual = ElectionType.FEDERAL;
        ElectionType expected = election.getElectionType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetTitle() throws Exception {
        String actual = "2017 Test Election";
        String expected = election.getTitle();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() throws Exception {
        String actual = "2017 Test Election (Sun Nov 05 00:00:00 EDT 2017)";
        String expected = election.toString();
        Assert.assertEquals(expected, actual);
    }

}