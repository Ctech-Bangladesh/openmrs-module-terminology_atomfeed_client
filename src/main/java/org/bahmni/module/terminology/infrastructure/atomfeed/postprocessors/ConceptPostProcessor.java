package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

import java.util.Collection;

public abstract class ConceptPostProcessor {
    public abstract ConceptType getConceptType();
    abstract Logger getLogger();
    abstract String getParentConceptName();

    @Autowired
    ConceptService conceptService;


    public void process(Concept concept) {
        Concept parentConcept = getParentConcept();
        if (parentConcept == null) {
            getLogger().info("Configuration required: Could not find concept -" + getParentConceptName());
            return;
        }

        if (!isPresentAsMember(parentConcept, concept)) {
            ConceptAnswer answer = new ConceptAnswer();
            answer.setAnswerConcept(concept);
            parentConcept.addAnswer(answer);
            conceptService.saveConcept(parentConcept);
        } else {
            getLogger().info("Concept is already a member of existing " + getParentConceptName());
        }
    }

    private Concept getParentConcept(){
        return conceptService.getConceptByName(getParentConceptName());
    }

     private boolean isPresentAsMember(Concept parentConcept, Concept concept) {
        Collection<ConceptAnswer> members = parentConcept.getAnswers();
        for (ConceptAnswer member : members) {
            if (StringUtils.equals(member.getConcept().getName().getName(), concept.getName().getName())) {
                return true;
            }
        }
        return false;
    }
}
