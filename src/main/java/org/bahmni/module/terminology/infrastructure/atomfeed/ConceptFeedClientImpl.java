package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptFeedWorker;
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
    private DiagnosisPostProcessor diagnosisPostProcessor;

    @Autowired
    public ConceptFeedClientImpl(ConceptSyncService ConceptSyncService, FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties,
                                 ConceptRequestMapper conceptRequestMapper,
                                 DiagnosisPostProcessor diagnosisPostProcessor) {
        this.ConceptSyncService = ConceptSyncService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptRequestMapper = conceptRequestMapper;
        this.diagnosisPostProcessor = diagnosisPostProcessor;
    }

    @Override
    public void syncDiangosis() throws URISyntaxException {
        logger.info("Sync Start: Diagnosis Terminology Concepts ..... ");
        feedProcessor.process(properties.getDiagnosisFeedUrl(), diagnosisWorker(), properties);
    }

    @Override
    public void retrySync() throws URISyntaxException {
        logger.info("Retrying failed concepts/diagnosis...");
        feedProcessor.retry(properties.getDiagnosisFeedUrl(), diagnosisWorker(), properties);
    }

    private ConceptFeedWorker diagnosisWorker() {
        return new ConceptFeedWorker(httpClient, properties, ConceptSyncService, conceptRequestMapper, ConceptType.Diagnosis);
    }
}
