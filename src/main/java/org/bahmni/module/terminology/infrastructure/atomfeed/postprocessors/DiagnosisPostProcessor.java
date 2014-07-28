package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.log4j.Logger;
import org.openmrs.Concept;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("diagnosisPostProcessor")
public class DiagnosisPostProcessor implements ConceptPostProcessor {

    @Autowired
    ConceptService conceptService;

    @Autowired
    @Qualifier("adminService")
    protected AdministrationService administrationService;

    public static final String UNCATEGORIZED_DIAGNOSES_NAME = "uncategorized diagnoses";

    private final Logger logger = Logger.getLogger(DiagnosisPostProcessor.class);

    @Override
    public void process(Concept concept) {
        Concept diagnosisSet = conceptService.getConceptByName(UNCATEGORIZED_DIAGNOSES_NAME);
        if (diagnosisSet == null) {
            logger.info("Configuration required: Could not find ConvSet - uncategorized diagnoses");
            return;
        }

        if (!isPresentAsMember(diagnosisSet, concept)) {
            diagnosisSet.addSetMember(concept);
            conceptService.saveConcept(diagnosisSet);
        } else {
            logger.info("Concept is already a member of existing " + UNCATEGORIZED_DIAGNOSES_NAME);
        }

    }

    private boolean isPresentAsMember(Concept parentConcept, Concept concept) {
        List<Concept> setMembers = parentConcept.getSetMembers();
        for (Concept member : setMembers) {
            if (member.getName().getName().equals(concept.getName().getName())) {
                return true;
            }
        }
        return false;
    }
}
