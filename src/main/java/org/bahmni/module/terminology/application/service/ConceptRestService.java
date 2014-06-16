package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mapper.ConceptMapper;
import org.bahmni.module.terminology.application.model.ConceptRestResource;
import org.bahmni.module.terminology.application.util.SimpleObjectUtil;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.resource.api.CrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConceptRestService {

    private RestService restService;
    private ConceptService conceptService;
    private ConceptMapper conceptMapper;

    @Autowired
    public ConceptRestService(RestService restService, ConceptService conceptService, ConceptMapper conceptMapper) {
        this.restService = restService;
        this.conceptService = conceptService;
        this.conceptMapper = conceptMapper;
    }

    public void save(SimpleObject simpleObject) throws IOException {
        SimpleObject conceptData = SimpleObjectUtil.toSimpleObject(conceptMapper.map(simpleObject));
        CrudResource conceptResource = (CrudResource) restService.getResourceByName("v1/concept");
        conceptResource.create(new ConceptRestResource(conceptData).toDTO(conceptService), null);
    }
}