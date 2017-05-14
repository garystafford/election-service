package com.voterapi.election.service;

import com.voterapi.election.domain.Election;
import com.voterapi.election.domain.ElectionType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ElectionDemoListService {

    private List<Election> elections;


    public ElectionDemoListService() {
        elections = new ArrayList<>();
        setElections();
    }

    private void setElections() {
        elections.add(new Election(
                new GregorianCalendar(2012, 11, 6).getTime(),
                ElectionType.FEDERAL,
                "2012 Presidential Election"));
        elections.add(new Election(
                new GregorianCalendar(2016, 11, 6).getTime(),
                ElectionType.FEDERAL,
                "2016 Presidential Election"));
        elections.add(new Election(
                new GregorianCalendar(2014, 11, 4).getTime(),
                ElectionType.STATE,
                "2014 New York Gubernatorial Election"));
        elections.add(new Election(
                new GregorianCalendar(2013, 11, 5).getTime(),
                ElectionType.LOCAL,
                "2013 New York City Mayoral Election"));
    }

    public List<Election> getElections() {
        elections.sort(Comparator.comparing(Election::getTitle));
        return elections;
    }
}
