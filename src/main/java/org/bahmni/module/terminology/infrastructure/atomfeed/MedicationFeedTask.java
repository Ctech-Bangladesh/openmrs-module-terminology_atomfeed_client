package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.net.URISyntaxException;

public class MedicationFeedTask extends AbstractTask {

    @Override
    public void execute() {
        MedicationFeedClient medicationFeedClient = Context.getService(MedicationFeedClient.class);
        System.out.println("**************************************");
        System.out.println("Executing Sync For Medication");
        System.out.println("**************************************");
        try {
            medicationFeedClient.sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
