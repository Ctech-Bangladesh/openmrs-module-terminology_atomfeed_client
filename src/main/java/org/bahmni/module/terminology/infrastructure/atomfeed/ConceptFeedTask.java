package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ConceptFeedTask extends AbstractTask {

    private Logger logger = Logger.getLogger(ConceptFeedTask.class);

    @Override
    public void execute() {
        try {
            Context.getService(ConceptFeedClient.class).sync();
        } catch (RuntimeException exception) {
            logger.error(exception);
        }
    }

}
