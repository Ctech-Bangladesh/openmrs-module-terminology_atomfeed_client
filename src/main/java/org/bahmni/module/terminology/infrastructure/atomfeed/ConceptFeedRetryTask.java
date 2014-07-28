package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.net.URISyntaxException;

public class ConceptFeedRetryTask extends AbstractTask {

    @Override
    public void execute() {
        DiagnosisFeedClient diagnosisFeedClient = Context.getService(DiagnosisFeedClient.class);
        try {
            diagnosisFeedClient.retrySync();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
