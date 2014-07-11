package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.application.service.SHRConceptService;
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

    private SHRConceptService SHRConceptService;
    private FeedProcessor feedProcessor;
    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;
    private ConceptRequestMapper conceptRequestMapper;
    private DiagnosisPostProcessor diagnosisPostProcessor;

    @Autowired
    public ConceptFeedClientImpl(SHRConceptService SHRConceptService, FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties,
                                 ConceptRequestMapper conceptRequestMapper,
                                 DiagnosisPostProcessor diagnosisPostProcessor) {
        this.SHRConceptService = SHRConceptService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptRequestMapper = conceptRequestMapper;
        this.diagnosisPostProcessor = diagnosisPostProcessor;
    }

    @Override
    public void syncDiangosis() throws URISyntaxException {
        logger.info("Sync Start: Diagnosis Terminology Concepts ..... ");
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, properties, SHRConceptService, conceptRequestMapper, diagnosisPostProcessor, ConceptType.Diagnosis);
        feedProcessor.process(properties.getDiagnosisFeedUrl(), worker, properties);
    }
}
