package org.bahmni.module.terminology.feeds;


import java.net.URISyntaxException;

public interface ConceptFeedClient {
    void syncConcepts() throws URISyntaxException;
}
