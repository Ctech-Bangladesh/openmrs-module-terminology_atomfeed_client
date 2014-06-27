package org.bahmni.module.terminology.application.service;

import com.google.gson.Gson;
import org.bahmni.module.terminology.application.mapper.ConceptMapper;
import org.bahmni.module.terminology.application.dtos.ConceptRestResource;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.CrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConceptRestService {

    private RestService restService;
    private ConceptMapper conceptMapper;

    @Autowired
    public ConceptRestService(RestService restService, ConceptMapper conceptMapper) {
        this.restService = restService;
        this.conceptMapper = conceptMapper;
    }

    public void save(SimpleObject simpleObject) throws IOException {
        SimpleObject conceptData = toSimpleObject(conceptMapper.map(simpleObject));
        CrudResource conceptResource = (CrudResource) restService.getResourceByName("v1/concept");
        conceptResource.create(new ConceptRestResource(conceptData).toDTO(), null);
    }

    public static SimpleObject toSimpleObject(Object entity) throws IOException {
        return SimpleObject.parseJson(new Gson().toJson(entity));
    }

}