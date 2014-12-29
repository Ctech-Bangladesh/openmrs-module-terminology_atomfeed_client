package org.bahmni.module.terminology.infrastructure.atomfeed;


import java.net.URISyntaxException;

public interface MedicationFeedClient {
    public void sync();
    public void retrySync();
}
