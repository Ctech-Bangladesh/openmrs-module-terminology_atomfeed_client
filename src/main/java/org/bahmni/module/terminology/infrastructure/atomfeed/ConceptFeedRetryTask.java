package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

import java.net.URISyntaxException;

public class ConceptFeedRetryTask extends AbstractTask {

    public static final int RETRY_COUNT = 3;
    private Logger logger = Logger.getLogger(ConceptFeedRetryTask.class);

    @Override
    public void execute() {
        retryFailedRefTerms();
        retryFailedConcepts();
        retryFailedMedications();
    }

    /**
     * The loop is because atomfeed client library currently can only process 5
     * failed events as a batch each time. Should be fixed in AtomFeedClient
     *
     */
    private void retryFailedMedications() {
        for (int count = 1; count <= RETRY_COUNT ; count++) {
            try {
                Context.getService(MedicationFeedClient.class).retrySync();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private void retryFailedConcepts() {
        for (int count = 1; count <= RETRY_COUNT ; count++) {
            try {
                Context.getService(ConceptFeedClient.class).retrySync();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private void retryFailedRefTerms() {
        for (int count = 1; count <= RETRY_COUNT ; count++) {
            try {
                Context.getService(ConceptReferenceTermFeedClient.class).retrySync();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

}
