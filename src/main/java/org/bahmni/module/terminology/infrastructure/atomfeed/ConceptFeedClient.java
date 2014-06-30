package org.bahmni.module.terminology.infrastructure.atomfeed;

import java.net.URISyntaxException;

public interface ConceptFeedClient {

    public void syncAllConcepts() throws URISyntaxException;

}
