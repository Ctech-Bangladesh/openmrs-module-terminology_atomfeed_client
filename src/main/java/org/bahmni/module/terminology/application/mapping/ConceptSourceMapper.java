package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConceptSourceMapper {

    private ConceptService conceptService;

    @Autowired
    public ConceptSourceMapper(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public ConceptSource map(ConceptSourceRequest conceptSourceRequest) {
        ConceptSource conceptSource = conceptService.getConceptSourceByName(conceptSourceRequest.getName());
        if (null != conceptSource) {
            return conceptSource;
        } else {
            ConceptSource newConceptSource = new ConceptSource();
            newConceptSource.setName(conceptSourceRequest.getName());
            newConceptSource.setHl7Code(conceptSourceRequest.getHl7Code());
            newConceptSource.setDescription(conceptSourceRequest.getDescription());
            return newConceptSource;
        }
    }
}
