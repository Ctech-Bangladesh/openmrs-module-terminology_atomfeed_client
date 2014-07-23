package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.net.URISyntaxException;

public class ConceptReferenceTermFeedTask extends AbstractTask {

    @Override
    public void execute() {
        ConceptReferenceTermFeedClient referenceTermFeedClient = Context.getService(ConceptReferenceTermFeedClient.class);
        try {
            referenceTermFeedClient.sync();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
