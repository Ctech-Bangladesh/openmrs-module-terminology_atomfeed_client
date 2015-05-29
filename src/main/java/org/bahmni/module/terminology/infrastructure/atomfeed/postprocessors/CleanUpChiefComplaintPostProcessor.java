package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("defaultChiefComplaintPostProcessor")
public class CleanUpChiefComplaintPostProcessor extends ConceptPostProcessor {

    private final Logger logger = Logger.getLogger(CleanUpChiefComplaintPostProcessor.class);

    @Override
    public ConceptType getConceptType() {
        return ConceptType.Default;
    }

    @Override
    public ProcessingInfo getProcessingInfo() {
        return ProcessingInfo.CLEANUP_CODEDANSWER;
    }

    @Override
    Logger getLogger() {
        return logger;
    }

    @Override
    String getParentConceptName() {
        return "Chief Complaint Answers";
    }
}
