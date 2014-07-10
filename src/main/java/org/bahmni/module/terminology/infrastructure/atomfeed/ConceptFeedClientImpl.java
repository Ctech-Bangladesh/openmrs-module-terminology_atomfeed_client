package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.mappers.BasicConceptMapper;
import org.bahmni.module.terminology.application.mappers.DiagnosisMapper;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.NOPPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptFeedWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    private final Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);

    private ConceptService conceptService;
    private FeedProcessor feedProcessor;
    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;
    private BasicConceptMapper conceptMapper;
    private DiagnosisMapper diagnosisMapper;
    private DiagnosisPostProcessor diagnosisPostProcessor;

    @Autowired
    public ConceptFeedClientImpl(ConceptService conceptService, FeedProcessor feedProcessor,
                                 AuthenticatedHttpClient httpClient,
                                 TRFeedProperties properties,
                                 BasicConceptMapper conceptMapper,
                                 @Qualifier("terminologyDiagnosisMapper") DiagnosisMapper diagnosisMapper,
                                 DiagnosisPostProcessor diagnosisPostProcessor) {
        this.conceptService = conceptService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
        this.conceptMapper = conceptMapper;
        this.diagnosisMapper = diagnosisMapper;
        this.diagnosisPostProcessor = diagnosisPostProcessor;
    }

    @Override
    public void syncAllConcepts() throws URISyntaxException {
        logger.info("Sync Start: All Terminology Concepts ..... ");
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, properties, conceptService, conceptMapper, new NOPPostProcessor());
        feedProcessor.process(properties.terminologyFeedUri(), worker, properties);
    }

    @Override
    public void syncDiangosis() throws URISyntaxException {
        logger.info("Sync Start: Diagnosis Terminology Concepts ..... ");
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, properties, conceptService, diagnosisMapper, diagnosisPostProcessor);
        feedProcessor.process(properties.getDiagnosisFeedUrl(), worker, properties);
    }


}
