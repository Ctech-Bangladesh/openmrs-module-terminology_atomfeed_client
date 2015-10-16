package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.SHReferenceTermService;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptReferenceTermEventWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptReferenceTermRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("conceptReferenceTermFeedClient")
public class ConceptReferenceTermFeedClientImpl implements ConceptReferenceTermFeedClient {

    private final Logger logger = Logger.getLogger(ConceptReferenceTermFeedClientImpl.class);

    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;
    private SHReferenceTermService shReferenceTermService;
    private ConceptReferenceTermRequestMapper conceptReferenceTermRequestMapper;
    private FeedProcessor feedProcessor;

    @Autowired
    public ConceptReferenceTermFeedClientImpl(AuthenticatedHttpClient httpClient,
                                              TRFeedProperties properties,
                                              SHReferenceTermService shReferenceTermService,
                                              ConceptReferenceTermRequestMapper conceptReferenceTermRequestMapper,
                                              FeedProcessor feedProcessor) {
        this.httpClient = httpClient;
        this.properties = properties;
        this.shReferenceTermService = shReferenceTermService;
        this.conceptReferenceTermRequestMapper = conceptReferenceTermRequestMapper;
        this.feedProcessor = feedProcessor;
    }

    @Override
    public void sync() throws URISyntaxException {
        logger.info("Sync Start: Concept Reference Terms ..... ");
        feedProcessor.process(properties.getReferenceTermFeedUrl(), getConceptReferenceTermEventWorker(), properties);
    }

    @Override
    public void retrySync() throws URISyntaxException {
        feedProcessor.retry(properties.getReferenceTermFeedUrl(), getConceptReferenceTermEventWorker(), properties);
    }

    private ConceptReferenceTermEventWorker getConceptReferenceTermEventWorker() {
        return new ConceptReferenceTermEventWorker(httpClient, properties, shReferenceTermService, conceptReferenceTermRequestMapper);
    }
}
