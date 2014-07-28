package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptFeedWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("chiefComplaintFeedClient")
public class ChiefComplaintFeedClientImpl implements ChiefComplaintFeedClient {

    private final Logger logger = Logger.getLogger(ChiefComplaintFeedClientImpl.class);

    private ConceptSyncService ConceptSyncService;
    private FeedProcessor feedProcessor;
    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;
    private ConceptRequestMapper conceptRequestMapper;

    @Autowired
    public ChiefComplaintFeedClientImpl(ConceptSyncService ConceptSyncService, FeedProcessor feedProcessor,
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
        logger.info("Sync Start: Diagnosis Terminology Concepts ..... ");
        for (String url : properties.getChiefComplaintFeedUrls()) {
            try {
                feedProcessor.process(url, worker(), properties);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void retrySync() {
        logger.info("Retrying failed concepts/diagnosis...");
        for (String url : properties.getChiefComplaintFeedUrls()) {
            try {
                feedProcessor.retry(url, worker(), properties);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ConceptFeedWorker worker() {
        return new ConceptFeedWorker(httpClient, properties, ConceptSyncService, conceptRequestMapper);
    }
}
