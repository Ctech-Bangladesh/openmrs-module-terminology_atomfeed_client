package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("diagnosisPostProcessor")
public class DiagnosisPostProcessor extends ConceptPostProcessor {


    private final Logger logger = Logger.getLogger(DiagnosisPostProcessor.class);

    @Override
    public ConceptType getConceptType(){
        return ConceptType.Diagnosis;
    }

    @Override
    public ProcessingInfo getProcessingInfo() {
        return ProcessingInfo.SETMEMBER;
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    String getParentConceptName() {
        return "uncategorized diagnoses";
    }


}
