package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.openmrs.Concept;

public class NOPPostProcessor implements ConceptPostProcessor {
    @Override
    public void process(Concept concept) {
        //do nothing
    }
}
