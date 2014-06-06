package org.bahmni.module.terminology.feeds.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.bahmni.module.terminology.factory.HttpClientFactory;
import org.bahmni.module.terminology.factory.TRPropertiesFactory;
import org.bahmni.module.terminology.feeds.ConceptFeedClient;
import org.bahmni.module.terminology.worker.ConceptFeedWorker;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.openmrs.api.context.Context;
import org.openmrs.module.atomfeed.transaction.support.AtomFeedSpringTransactionManager;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;


@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    @Resource(name = "terminologyFeedProperties")
    private Properties defaultAtomFeedProperties;
    private RestService restService;
    private PlatformTransactionManager transactionManager;
    private HttpClientFactory httpClientFactory;

    private final Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);

    @Autowired
    public ConceptFeedClientImpl(RestService restService, PlatformTransactionManager transactionManager, HttpClientFactory httpClientFactory) {
        this.restService = restService;
        this.transactionManager = transactionManager;
        this.httpClientFactory = httpClientFactory;
    }

    @Override
    public void syncConcepts() throws URISyntaxException {
        logger.info("Terminology atom feed started!");
        TRFeedProperties properties = new TRPropertiesFactory(defaultAtomFeedProperties).build();
        AtomFeedSpringTransactionManager txManager = new AtomFeedSpringTransactionManager(transactionManager);
        AtomFeedClient atomFeedClient = new AtomFeedClient(
                new AllFeeds(properties, new HashMap<String, String>()),
                new AllMarkersJdbcImpl(txManager),
                new AllFailedEventsJdbcImpl(txManager),
                properties,
                txManager,
                new URI(properties.terminologyFeedUri()),
                new ConceptFeedWorker(httpClientFactory.createAuthenticatedHttpClient(), restService, properties, Context.getConceptService()));
        atomFeedClient.processEvents();
    }
}
