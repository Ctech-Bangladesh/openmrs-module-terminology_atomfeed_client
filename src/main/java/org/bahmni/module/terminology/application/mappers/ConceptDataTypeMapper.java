package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConceptDataTypeMapper implements Mapper<ConceptDatatype> {

    private ConceptService conceptService;

    @Autowired
    public ConceptDataTypeMapper(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public ConceptDatatype map(Map<String, Object> data) {
        if (data != null) {
            return conceptService.getConceptDatatypeByName(data.get("name").toString());
        }
        return null;
    }
}
