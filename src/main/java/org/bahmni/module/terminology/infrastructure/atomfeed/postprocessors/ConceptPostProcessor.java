package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.bahmni.module.terminology.application.model.ConceptType;
import org.openmrs.Concept;

public interface ConceptPostProcessor {
    void process(Concept concept);
    ConceptType getConceptType();
}
