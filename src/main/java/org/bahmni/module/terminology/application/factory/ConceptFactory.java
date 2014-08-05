package org.bahmni.module.terminology.application.factory;

import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptNumeric;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class ConceptFactory {

    private ConceptService conceptService;

    @Autowired
    public ConceptFactory(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public Concept createConcept(ConceptRequest conceptRequest) {
        if (isNotBlank(conceptRequest.getDatatypeName())) {
            ConceptDatatype datatype = conceptService.getConceptDatatypeByName(conceptRequest.getDatatypeName());
            if (datatype.isNumeric()) {
                return new ConceptNumeric();
            }
        }
        return new Concept();
    }
}
