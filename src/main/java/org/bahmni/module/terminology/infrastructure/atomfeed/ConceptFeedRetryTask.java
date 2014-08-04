package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ConceptFeedRetryTask extends AbstractTask {

    private Logger logger = Logger.getLogger(ConceptFeedRetryTask.class);

    @Override
    public void execute() {
        try {
            Context.getService(ConceptFeedClient.class).retrySync();
        } catch (RuntimeException exception) {
            logger.error(exception);
        }
    }

}
