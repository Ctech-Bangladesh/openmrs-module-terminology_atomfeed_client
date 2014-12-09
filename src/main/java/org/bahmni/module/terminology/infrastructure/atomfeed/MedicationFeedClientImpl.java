package org.bahmni.module.terminology.infrastructure.atomfeed;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component("medicationFeedClient")
public class MedicationFeedClientImpl implements MedicationFeedClient {
    private final Logger logger = Logger.getLogger(MedicationFeedClientImpl.class);
    private final FeedProcessor feedProcessor;
    private final AuthenticatedHttpClient httpClient;
    private final TRFeedProperties properties;

    @Autowired
    public MedicationFeedClientImpl(FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties) {
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
    }


    @Override
    public void sync() throws URISyntaxException {
        logger.info("Sync Start: Concepts ..... ");
        try {
            feedProcessor.process(properties.medicationFeedUri(), medicationEventWorker(), properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private EventWorker medicationEventWorker() {
        return new EventWorker() {
            @Override
            public void process(Event event) {
                System.out.println("**********************************");
                System.out.println("Processing event ... " + event.getContent());
                System.out.println("**********************************");
            }

            @Override
            public void cleanUp(Event event) {

            }
        };
    }
}
