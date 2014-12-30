package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.MedicationEventWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component("medicationFeedClient")
public class MedicationFeedClientImpl implements MedicationFeedClient {
    private final Logger logger = Logger.getLogger(MedicationFeedClientImpl.class);
    private final FeedProcessor feedProcessor;
    private final AuthenticatedHttpClient httpClient;
    private final TRFeedProperties properties;
    private ConceptService conceptService;
    private IdMappingsRepository idMapper;

    @Autowired
    public MedicationFeedClientImpl(FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties,
                                 ConceptService conceptService,
                                 IdMappingsRepository idMapper) {
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptService = conceptService;
        this.idMapper = idMapper;
    }


    @Override
    public void sync(){
        logger.info("Sync Start: Medication ..... ");
        try {
            feedProcessor.process(properties.medicationFeedUri(), medicationEventWorker(), properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void retrySync() {
        logger.info(" Retry Sync Start: Medication ..... ");
        try {
            feedProcessor.retry(properties.medicationFeedUri(), medicationEventWorker(), properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private EventWorker medicationEventWorker() {
        return new MedicationEventWorker(properties, httpClient, conceptService, idMapper);
    }
}
