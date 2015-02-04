package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

public class MedicationFeedTask extends AbstractTask {

    @Override
    public void execute() {
        MedicationFeedClient medicationFeedClient = Context.getService(MedicationFeedClient.class);
        try {
            medicationFeedClient.sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
