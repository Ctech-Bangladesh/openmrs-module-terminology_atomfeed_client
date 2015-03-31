package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.stereotype.Component;

@Component("procedurePostProcessor")
public class ProcedurePostProcessor extends ConceptPostProcessor {

    private final Logger logger = Logger.getLogger(ProcedurePostProcessor.class);

    @Override
    public String getParentConceptName(){
        return "Procedure";
    }


    @Override
    public ConceptType getConceptType() {
        return ConceptType.Procedure;
    }

    @Override
    public ProcessingInfo getProcessingInfo() {
        return ProcessingInfo.CODEDANSWER;
    }

    @Override
    public Logger getLogger(){
        return logger;
    }


}
