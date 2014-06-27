package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.bahmni.module.terminology.infrastructure.atomfeed.workers.DiagnosisFeedWorker;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component("diagnosisFeedClient")
public class DiagnosisFeedClientImpl implements DiagnosisFeedClient {

    private final Logger logger = Logger.getLogger(DiagnosisFeedClientImpl.class);

    private final ConceptRestService conceptRestService;
    private final FeedProcessor feedProcessor;
    private final AuthenticatedHttpClient httpClient;
    private final TRFeedProperties properties;

    @Autowired
    public DiagnosisFeedClientImpl(ConceptRestService conceptRestService, FeedProcessor feedProcessor,  AuthenticatedHttpClient httpClient, TRFeedProperties properties) {
        this.conceptRestService = conceptRestService;
        this.feedProcessor = feedProcessor;
        this.httpClient = httpClient;
        this.properties = properties;
    }

    @Override
    public void syncDiagnosis() throws URISyntaxException {
        logger.info("starting to sync diagnosis from terminology server...");
        feedProcessor.process(properties.getDiagnosisFeedUrl(), new DiagnosisFeedWorker(), properties);
    }
}
