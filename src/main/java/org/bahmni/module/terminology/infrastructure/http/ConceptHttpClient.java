package org.bahmni.module.terminology.infrastructure.http;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.terminology.infrastructure.factory.HttpClientFactory;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConceptHttpClient {

    private HttpClient authenticatedHttpClient;

    @Autowired
    public ConceptHttpClient(HttpClientFactory clientFactory) {
        this.authenticatedHttpClient = clientFactory.createAuthenticatedHttpClient();
    }

    public SimpleObject get(String url) throws IOException {
        HttpResponse response = authenticatedHttpClient.execute(new HttpGet(url));
        return SimpleObject.parseJson(EntityUtils.toString(response.getEntity()));
    }
}
