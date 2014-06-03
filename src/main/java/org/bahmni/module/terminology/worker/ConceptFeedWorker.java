package org.bahmni.module.terminology.worker;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;


public class ConceptFeedWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptFeedWorker.class);

    public ConceptFeedWorker(TRFeedProperties properties) {
    }

    @Override
    public void process(Event event) {
        logger.info("Processing event " + event.getFeedUri() + " with content " + event.getContent());
    }

    @Override
    public void cleanUp(Event event) {

    }
}
