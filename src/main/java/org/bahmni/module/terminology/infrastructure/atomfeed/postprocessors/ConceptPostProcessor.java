package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public abstract class ConceptPostProcessor {
    public abstract ConceptType getConceptType();

    public abstract ProcessingInfo getProcessingInfo();

    abstract Logger getLogger();

    abstract String getParentConceptName();

    @Autowired
    ConceptService conceptService;

    private List<String> matchingClasses = Arrays.asList("symptom", "symptom/finding");

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
        } else if (processingInfo.equals(ProcessingInfo.CLEANUP_CODEDANSWER)) {
            cleanUp(parentConcept, concept);
        }
    }

    private void addAsMember(Concept parentConcept, Concept concept) {
        if (parentConcept.getSet()) {
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

    private Concept getParentConcept() {
        List<Concept> conceptsByName = conceptService.getConceptsByName(getParentConceptName(), Locale.ENGLISH, false);
        return conceptsByName.size() > 0 ? conceptsByName.get(0) : null;
        //return conceptService.getConceptByName(getParentConceptName());
    }

    private boolean isPresentAsAnswer(Concept parentConcept, Concept concept) {
        Collection<ConceptAnswer> members = parentConcept.getAnswers();
        for (ConceptAnswer member : members) {
            if (member.getAnswerConcept().getUuid().equals(concept.getUuid())) {
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

    private void cleanUp(Concept parentConcept, Concept concept) {
        if (matchingClasses.contains(concept.getConceptClass().getName().toLowerCase())) {
            return;
        }
        if (parentConcept.getDatatype().isCoded()) {
            ConceptAnswer present = getAnswerIfPresent(parentConcept, concept);
            if (null != present) {
                parentConcept.removeAnswer(present);
                conceptService.saveConcept(parentConcept);
            } else {
                getLogger().info(String.format("Concept is not an answer of parent [%s]", getParentConceptName()));
            }
        } else {
            getLogger().info(String.format("Can not remove answer as the Parent [%s] dataType is not coded",getParentConceptName()));
        }
    }

    private ConceptAnswer getAnswerIfPresent(Concept parentConcept, Concept concept) {
        Collection<ConceptAnswer> members = parentConcept.getAnswers();
        for (ConceptAnswer member : members) {
            if (member.getAnswerConcept().getUuid().equals(concept.getUuid())) {
                return member;
            }
        }
        return null;
    }
}
