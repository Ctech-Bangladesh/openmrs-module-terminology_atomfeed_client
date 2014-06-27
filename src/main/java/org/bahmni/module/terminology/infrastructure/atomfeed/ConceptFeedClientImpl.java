package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.ConceptFeedWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    private ConceptRestService conceptRestService;
    private FeedProcessor feedProcessor;
    private AuthenticatedHttpClient httpClient;
    private TRFeedProperties properties;

    private final Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);

    @Autowired
    public ConceptFeedClientImpl(ConceptRestService conceptRestService, FeedProcessor feedProcessor,  AuthenticatedHttpClient httpClient, TRFeedProperties properties) {
        this.conceptRestService = conceptRestService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
    }

    @Override
    public void syncConcepts() throws URISyntaxException {
        logger.info("Terminology atom feed started!");
        feedProcessor.process(properties.terminologyFeedUri(), new ConceptFeedWorker(httpClient, properties, conceptRestService), properties);
    }
}
