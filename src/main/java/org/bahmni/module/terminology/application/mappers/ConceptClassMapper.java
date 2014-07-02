package org.bahmni.module.terminology.application.mappers;


import org.openmrs.ConceptClass;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConceptClassMapper implements Mapper<ConceptClass> {

    private ConceptService conceptService;

    @Autowired
    public ConceptClassMapper(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public ConceptClass map(Map<String, Object> data) {
        return data == null ? null : conceptService.getConceptClassByName(data.get("name").toString());
    }
}
