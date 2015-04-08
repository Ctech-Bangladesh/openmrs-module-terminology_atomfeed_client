package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("diagnosisAsSetMemberPostProcessor")
public class DiagnosisAsSetMemberPostProcessor extends ConceptPostProcessor {


    private final Logger logger = Logger.getLogger(DiagnosisAsSetMemberPostProcessor.class);

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
