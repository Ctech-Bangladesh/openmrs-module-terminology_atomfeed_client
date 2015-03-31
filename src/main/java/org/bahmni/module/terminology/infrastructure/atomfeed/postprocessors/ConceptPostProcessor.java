package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

public abstract class ConceptPostProcessor {
    public abstract ConceptType getConceptType();

    public abstract ProcessingInfo getProcessingInfo();

    abstract Logger getLogger();
    abstract String getParentConceptName();

    @Autowired
    ConceptService conceptService;


    public void process(Concept concept) {
        Concept parentConcept = getParentConcept();
        if (parentConcept == null) {
            getLogger().info(String.format("Configuration required. Could not find parent concept [%s]. Aborting post processing", getParentConceptName()));
            return;
        }

        ProcessingInfo processingInfo = getProcessingInfo();

        if (processingInfo.equals(ProcessingInfo.CODEDANSWER)) {
            addAsAnswers(parentConcept, concept);
        } else if (processingInfo.equals(ProcessingInfo.SETMEMBER)) {
            addAsMember(parentConcept, concept);
        }

    }

    private void addAsMember(Concept parentConcept, Concept concept) {
        if (parentConcept.isSet()) {
            if (!isPresentAsMember(parentConcept, concept)) {
                parentConcept.addSetMember(concept);
                conceptService.saveConcept(parentConcept);
            } else {
                getLogger().info(String.format("Concept is already a member of Parent [%s]", getParentConceptName()));
            }

        } else {
            getLogger().info(String.format("Can not add as member as the Parent [%s] is not a set",getParentConceptName()));
        }

    }

    private void addAsAnswers(Concept parentConcept, Concept concept) {
        if (parentConcept.getDatatype().isCoded()) {
            if (!isPresentAsAnswer(parentConcept, concept)) {
                ConceptAnswer answer = new ConceptAnswer();
                answer.setAnswerConcept(concept);
                parentConcept.addAnswer(answer);
                conceptService.saveConcept(parentConcept);
            } else {
                getLogger().info(String.format("Concept is already an answer of parent [%s]", getParentConceptName()));
            }
        } else {
            getLogger().info(String.format("Can not add as answer as the Parent [%s] dataType is not coded",getParentConceptName()));
        }
    }

    private Concept getParentConcept(){
        return conceptService.getConceptByName(getParentConceptName());
    }

     private boolean isPresentAsAnswer(Concept parentConcept, Concept concept) {
        Collection<ConceptAnswer> members = parentConcept.getAnswers();
        for (ConceptAnswer member : members) {
            //TODO: should do a uuid check
            if (StringUtils.equals(member.getConcept().getName().getName(), concept.getName().getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPresentAsMember(Concept parentConcept, Concept concept) {
        List<Concept> setMembers = parentConcept.getSetMembers();
        for (Concept setMember : setMembers) {
            if (setMember.getUuid().equals(concept.getUuid())) {
                return true;
            }
        }
        return false;
    }
}
