package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptClass;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiagnosisConceptClassMapper implements Mapper<ConceptClass>{

    @Autowired
    private ConceptService conceptService;

    @Override
    public ConceptClass map(Map<String, Object> data) {
        return conceptService.getConceptClassByName("Diagnosis");
    }
}