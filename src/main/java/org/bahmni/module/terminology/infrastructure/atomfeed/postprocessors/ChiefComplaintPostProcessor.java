package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("chiefComplaintPostProcessor")
public class ChiefComplaintPostProcessor extends ConceptPostProcessor {

    private final Logger logger = Logger.getLogger(ChiefComplaintPostProcessor.class);

    @Override
    public ConceptType getConceptType() {
        return ConceptType.ChiefComplaint;
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
