package org.bahmni.module.terminology.infrastructure.atomfeed;

import java.net.URISyntaxException;

public interface DiagnosisFeedClient {

    public void syncDiangosis() throws URISyntaxException;

    void retrySync() throws URISyntaxException;
}
