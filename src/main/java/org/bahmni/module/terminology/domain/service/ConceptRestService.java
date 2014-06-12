package org.bahmni.module.terminology.domain.service;

import org.bahmni.module.terminology.feedclient.worker.ConceptRestResource;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.CrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConceptRestService {

    private ConceptService conceptService;
    private RestService restService;

    @Autowired
    public ConceptRestService(RestService restService) {
        this.restService = restService;
        this.conceptService = Context.getConceptService();
    }

    public void save(SimpleObject simpleObject) throws IOException {
        SimpleObject conceptData = new ConceptRestResource(simpleObject).toDTO(conceptService);
        CrudResource conceptResource = (CrudResource) restService.getResourceByName("v1/concept");
        conceptResource.create(conceptData, null);
    }
}
