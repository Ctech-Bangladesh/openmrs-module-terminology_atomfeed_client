package org.bahmni.module.terminology.application.service;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SHRConceptSourceService {

    private ConceptService conceptService;

    @Autowired
    public SHRConceptSourceService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void createNonExistentSources(Concept concept) {
        Collection<ConceptMap> conceptMappings = concept.getConceptMappings();
        if (conceptMappings != null) {
            for (ConceptMap conceptMapping : conceptMappings) {
                ConceptSource conceptSource = conceptMapping.getConceptReferenceTerm().getConceptSource();
                if (conceptSource != null) {
                    ConceptSource conceptSourceByName = conceptService.getConceptSourceByName(conceptSource.getName());
                    if (conceptSourceByName != null) {
                        conceptMapping.getConceptReferenceTerm().setConceptSource(conceptSourceByName);
                    } else {
                        conceptMapping.getConceptReferenceTerm().setConceptSource(conceptService.saveConceptSource(conceptSource));
                    }
                }
            }
        }
    }
}
