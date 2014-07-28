package org.bahmni.module.terminology.application.postprocessor;

import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ConceptPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.openmrs.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostProcessorFactory {

    private DiagnosisPostProcessor diagnosisPostProcessor;

    @Autowired
    public PostProcessorFactory(DiagnosisPostProcessor diagnosisPostProcessor) {
        this.diagnosisPostProcessor = diagnosisPostProcessor;
    }

    public ConceptPostProcessor getPostProcessor(Concept savedConcept) {
        return diagnosisPostProcessor;
    }
}
