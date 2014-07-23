package org.bahmni.module.terminology.infrastructure.atomfeed;

import java.net.URISyntaxException;

public interface ConceptReferenceTermFeedClient {

    public void sync() throws URISyntaxException;

}
