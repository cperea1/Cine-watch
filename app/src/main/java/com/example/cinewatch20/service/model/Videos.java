package com.example.cinewatch20.service.model;

import java.util.List;

public class Videos {

    private List<VideoResults> results;

    public List<VideoResults> getResults() {
        return results;
    }

    public void setResults(List<VideoResults> videoResults) {
        this.results = videoResults;
    }
}
