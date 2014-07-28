package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ConceptFeedRetryTask extends AbstractTask {

    private Logger logger = Logger.getLogger(ConceptFeedRetryTask.class);

    @Override
    public void execute() {
        retry(Context.getService(DiagnosisFeedClient.class));
        retry(Context.getService(ChiefComplaintFeedClient.class));
    }

    private void retry(ConceptFeedClient conceptFeedClient) {
        try {
            conceptFeedClient.retrySync();
        } catch (RuntimeException exception) {
            logger.error(exception);
        }
    }
}
