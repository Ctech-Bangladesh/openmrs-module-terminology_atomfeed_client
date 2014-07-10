package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.openmrs.Concept;

public interface ConceptPostProcessor {
    void process(Concept concept);
}
