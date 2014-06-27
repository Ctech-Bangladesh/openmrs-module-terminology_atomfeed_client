package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;


public class ConceptFeedWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptFeedWorker.class);

    private TRFeedProperties properties;
    private AuthenticatedHttpClient httpClient;
    private ConceptRestService conceptRestService;

    public ConceptFeedWorker(AuthenticatedHttpClient httpClient, TRFeedProperties properties, ConceptRestService conceptRestService) {
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptRestService = conceptRestService;
    }

    @Override
    public void process(final Event event) {
        logger.info(format("Received concept sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        try {
            Map concept = httpClient.get(properties.getTerminologyUrl(event.getContent()), HashMap.class);
            conceptRestService.save(concept);
        } catch (IOException e) {
            logger.error(format("Error while syncing concept %s , reason : %s", event.getId(), e.getMessage()));
        }
    }

    @Override
    public void cleanUp(Event event) {

    }
}
