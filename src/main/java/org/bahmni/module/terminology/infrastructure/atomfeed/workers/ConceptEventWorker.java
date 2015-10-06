package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptNameRequest;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;


public class ConceptEventWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptEventWorker.class);

    private ConceptRequestMapper conceptRequestMapper;
    private TRFeedProperties properties;
    private ConceptSyncService conceptSyncService;
    private AuthenticatedHttpClient httpClient;

    public ConceptEventWorker(AuthenticatedHttpClient httpClient, TRFeedProperties properties, ConceptSyncService ConceptSyncService, ConceptRequestMapper conceptRequestMapper) {
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptSyncService = ConceptSyncService;
        this.conceptRequestMapper = conceptRequestMapper;
    }

    @Override
    public void process(final Event event) {
        logger.info(format("Received concept sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        Map conceptMap = httpClient.get(properties.getTerminologyUrl(event.getContent()), HashMap.class);
        conceptSyncService.sync(conceptRequestMapper.map(conceptMap));
    }

    @Override
    public void cleanUp(Event event) {

    }
}
