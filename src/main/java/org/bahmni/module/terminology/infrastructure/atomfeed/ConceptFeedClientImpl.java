package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptEventWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    private final Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);

    private ConceptSyncService ConceptSyncService;
    private FeedProcessor feedProcessor;
    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;
    private ConceptRequestMapper conceptRequestMapper;

    @Autowired
    public ConceptFeedClientImpl(ConceptSyncService ConceptSyncService, FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties,
                                 ConceptRequestMapper conceptRequestMapper) {
        this.ConceptSyncService = ConceptSyncService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptRequestMapper = conceptRequestMapper;
    }

    @Override
    public void sync() {
        logger.info("Sync Start: Concepts ..... ");
        try {
            feedProcessor.process(properties.terminologyFeedUri(), diagnosisWorker(), properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void retrySync() {
        logger.info("Retrying Failed Concept...");
        try {
            feedProcessor.retry(properties.terminologyFeedUri(), diagnosisWorker(), properties);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private ConceptEventWorker diagnosisWorker() {
        return new ConceptEventWorker(httpClient, properties, ConceptSyncService, conceptRequestMapper);
    }
}
