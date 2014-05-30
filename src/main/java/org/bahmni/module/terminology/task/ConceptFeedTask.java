package org.bahmni.module.terminology.task;


import org.bahmni.module.terminology.feeds.ConceptFeedClient;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class ConceptFeedTask extends AbstractTask {

    @Override
    public void execute() {
        ConceptFeedClient conceptFeedClient = Context.getService(ConceptFeedClient.class);
        conceptFeedClient.syncConcepts();
    }
}
