package com.voterapi.election.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Document
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Election implements Serializable {

    @Id
    private String id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private String electionType;
    private String title;
    private String description;

    public Election() {
        // unused constructor
    }

    public Election(Date date,
                    String electionType,
                    String title,
                    String description) {
        this.date = date;
        this.electionType = electionType;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getElectionType() {
        return electionType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getTitle(), getDate());
    }
}
