package org.bahmni.module.terminology.infrastructure.feedclient.worker;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;

import static java.lang.String.format;


public class ConceptFeedWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptFeedWorker.class);

    private TRFeedProperties properties;
    private HttpClient authenticatedHttpClient;
    private ConceptRestService conceptRestService;

    public ConceptFeedWorker(HttpClient authenticatedHttpClient, TRFeedProperties properties, ConceptRestService conceptRestService) {
        this.authenticatedHttpClient = authenticatedHttpClient;
        this.properties = properties;
        this.conceptRestService = conceptRestService;
    }

    private SimpleObject asSimpleObject(HttpEntity entity) throws IOException {
        return new Gson().fromJson(EntityUtils.toString(entity), SimpleObject.class);
    }

    @Override
    public void process(final Event event) {
        logger.info(format("Received concept sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        try {
            HttpResponse response = authenticatedHttpClient.execute(new HttpGet(properties.getTerminologyUrl(event.getContent())));
            SimpleObject concept = asSimpleObject(response.getEntity());
            conceptRestService.save(concept);
        } catch (IOException e) {
            logger.error(format("Error while syncing concept %s , reason : %s", event.getId(), e.getMessage()));
        }
    }

    @Override
    public void cleanUp(Event event) {

    }
}
