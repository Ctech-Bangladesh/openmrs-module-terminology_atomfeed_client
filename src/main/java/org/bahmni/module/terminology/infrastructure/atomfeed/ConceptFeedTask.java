package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ConceptFeedTask extends AbstractTask {

    private Logger logger = Logger.getLogger(ConceptFeedTask.class);

    @Override
    public void execute() {
        sync(Context.getService(DiagnosisFeedClient.class));
        sync(Context.getService(ChiefComplaintFeedClient.class));
    }

    private void sync(ConceptFeedClient feedClient) {
        try {
            feedClient.sync();
        } catch (RuntimeException exception) {
            logger.error(exception);
        }
    }
}
