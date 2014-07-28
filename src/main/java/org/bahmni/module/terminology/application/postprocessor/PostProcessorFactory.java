package org.bahmni.module.terminology.application.postprocessor;

import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ChiefComplaintPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ConceptPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.openmrs.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostProcessorFactory {

    private DiagnosisPostProcessor diagnosisPostProcessor;
    private ChiefComplaintPostProcessor chiefComplaintPostProcessor;

    @Autowired
    public PostProcessorFactory(DiagnosisPostProcessor diagnosisPostProcessor, ChiefComplaintPostProcessor chiefComplaintPostProcessor) {
        this.diagnosisPostProcessor = diagnosisPostProcessor;
        this.chiefComplaintPostProcessor = chiefComplaintPostProcessor;
    }

    public ConceptPostProcessor getPostProcessor(Concept savedConcept) {
        if (ConceptType.Diagnosis.matches(savedConcept.getConceptClass().getName())) {
            return diagnosisPostProcessor;
        } else if (ConceptType.ChiefComplaint.matches(savedConcept.getConceptClass().getName())) {
            return chiefComplaintPostProcessor;
        }
        return null;
    }
}
