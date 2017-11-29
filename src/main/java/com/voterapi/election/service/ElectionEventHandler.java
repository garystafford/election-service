package com.voterapi.election.service;

import com.voterapi.election.domain.Election;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ElectionEventHandler {

    private ElectionService electionService;

    @Autowired
    public ElectionEventHandler(ElectionService electionService) {
        this.electionService = electionService;
    }

    @HandleAfterCreate
    public void handleElectionCreate(Election elections) {
        electionService.sendMessageAzureServiceBus(elections);
    }
}
