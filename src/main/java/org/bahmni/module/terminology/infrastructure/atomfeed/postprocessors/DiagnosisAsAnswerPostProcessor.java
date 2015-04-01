package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("diagnosisAsAnswerPostProcessor")
public class DiagnosisAsAnswerPostProcessor extends ConceptPostProcessor {


    private final Logger logger = Logger.getLogger(DiagnosisAsAnswerPostProcessor.class);

    @Override
    public ConceptType getConceptType(){
        return ConceptType.Diagnosis;
    }

    @Override
    public ProcessingInfo getProcessingInfo() {
        return ProcessingInfo.CODEDANSWER;
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    String getParentConceptName() {
        return "Diagnosis Answers";
    }


}
