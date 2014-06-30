package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mappers.ConceptMapper;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConceptRestService {

    private ConceptMapper conceptMapper;
    private ConceptService conceptService;

    @Autowired
    public ConceptRestService(ConceptMapper conceptMapper, ConceptService conceptService) {
        this.conceptMapper = conceptMapper;
        this.conceptService = conceptService;
    }

    public void save(Map conceptData) {
        Concept concept = conceptMapper.map((Map<String, Object>) conceptData, conceptService);
        conceptService.saveConcept(concept);
    }


}