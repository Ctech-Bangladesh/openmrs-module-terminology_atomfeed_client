package org.bahmni.module.terminology.feeds.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.bahmni.module.terminology.feeds.ConceptFeedClient;
import org.bahmni.module.terminology.worker.ConceptFeedWorker;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.atomfeed.transaction.support.AtomFeedSpringTransactionManager;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    private RestService restService;
    private PlatformTransactionManager transactionManager;
    private TRFeedProperties properties;

    public ConceptFeedClientImpl() {

    }

    @Autowired
    public ConceptFeedClientImpl(TRFeedProperties properties, RestService restService, PlatformTransactionManager transactionManager) {
        this.properties = properties;
        this.restService = restService;
        this.transactionManager = transactionManager;
    }

    @Override
    public void syncConcepts() throws URISyntaxException {
        Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);
        logger.info("Terminology atom feed started!");
        AtomFeedSpringTransactionManager txManager = new AtomFeedSpringTransactionManager(transactionManager);
        AtomFeedClient atomFeedClient = new AtomFeedClient(
                new AllFeeds(properties, new HashMap<String, String>()),
                new AllMarkersJdbcImpl(txManager),
                new AllFailedEventsJdbcImpl(txManager),
                properties,
                txManager,
                new URI(properties.terminologyFeedUri()),
                new ConceptFeedWorker(restService, properties, Context.getConceptService()));
        atomFeedClient.processEvents();
    }
}
