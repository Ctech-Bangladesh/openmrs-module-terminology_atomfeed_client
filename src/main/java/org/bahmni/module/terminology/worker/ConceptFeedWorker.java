package org.bahmni.module.terminology.worker;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.CrudResource;

import java.io.IOException;

import static java.lang.String.format;


public class ConceptFeedWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptFeedWorker.class);

    private RestService restService;
    private TRFeedProperties properties;
    private ConceptService conceptService;

    public ConceptFeedWorker(RestService restService, TRFeedProperties properties, ConceptService conceptService) {
        this.restService = restService;
        this.properties = properties;
        this.conceptService = conceptService;
    }

    private SimpleObject asSimpleObject(HttpEntity entity) throws IOException {
        return new Gson().fromJson(EntityUtils.toString(entity), SimpleObject.class);
    }

    private String getAbsolutePath(Event event) {
        return properties.terminologyServerPrefix() + event.getContent();
    }

    @Override
    public void process(final Event event) {
        logger.info(format("Received concept sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        try {
            HttpResponse response = httpClient().execute(new HttpGet(getAbsolutePath(event)));
            SimpleObject conceptData = new ConceptRestResource(asSimpleObject(response.getEntity())).toDTO(conceptService);
            CrudResource conceptResource = (CrudResource) restService.getResourceByName("v1/concept");
            conceptResource.create(conceptData, null);
        } catch (IOException e) {
            logger.error(format("Error while syncing concept %s , reason : %s", event.getId(), e.getMessage()));
        }
    }

    private HttpClient httpClient() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        //TODO: secure this
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "Admin123");
        provider.setCredentials(AuthScope.ANY, credentials);
        return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
    }

    @Override
    public void cleanUp(Event event) {

    }
}
