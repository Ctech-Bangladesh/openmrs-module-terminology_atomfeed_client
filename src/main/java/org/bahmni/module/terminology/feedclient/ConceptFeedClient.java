package org.bahmni.module.terminology.feedclient;


import java.net.URISyntaxException;

public interface ConceptFeedClient {
    void syncConcepts() throws URISyntaxException;
}
