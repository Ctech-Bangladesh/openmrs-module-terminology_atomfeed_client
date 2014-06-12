package org.bahmni.module.terminology.infrastructure.feedclient;


import java.net.URISyntaxException;

public interface ConceptFeedClient {
    void syncConcepts() throws URISyntaxException;
}
