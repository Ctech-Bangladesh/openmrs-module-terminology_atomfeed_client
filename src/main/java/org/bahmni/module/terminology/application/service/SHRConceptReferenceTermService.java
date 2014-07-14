package org.bahmni.module.terminology.application.service;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SHRConceptReferenceTermService {

    private ConceptService conceptService;

    @Autowired
    public SHRConceptReferenceTermService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void mergeExistingConceptReferenceTerms(Concept newConcept) {
        Collection<ConceptMap> conceptMappings = newConcept.getConceptMappings();
        if (null != conceptMappings) {
            for (ConceptMap conceptMap : conceptMappings) {
                ConceptReferenceTerm referenceTerm = conceptMap.getConceptReferenceTerm();
                ConceptReferenceTerm existingTerm = conceptService.getConceptReferenceTermByName(referenceTerm.getName(), getConceptSource(referenceTerm));
                if (null != existingTerm) {
                    conceptMap.setConceptReferenceTerm(existingTerm);
                }
            }
        }
    }

    private ConceptSource getConceptSource(ConceptReferenceTerm referenceTerm) {
        ConceptSource source = referenceTerm.getConceptSource();
        if (null != source) {
            return conceptService.getConceptSourceByName(source.getName());
        }
        return null;
    }
}
