package org.bahmni.module.terminology.infrastructure.atomfeed;

public interface ConceptFeedClient {

    public void sync();

    void retrySync();
}
