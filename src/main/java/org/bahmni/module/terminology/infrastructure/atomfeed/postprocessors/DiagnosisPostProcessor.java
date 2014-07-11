package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.commons.lang.StringUtils;
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

    private static final String GP_DIAGNOSIS_SET_OF_SETS = "emr.concept.diagnosisSetOfSets";
    public static final String UNCATEGORIZED_DIAGNOSES_NAME = "uncategorized diagnoses";

    private final Logger logger = Logger.getLogger(DiagnosisPostProcessor.class);

    @Override
    public void process(Concept concept) {
//        String rootDiagnosisSetUuid = getGlobalProperty(GP_DIAGNOSIS_SET_OF_SETS, false);
//        Concept rootDiagnosisConcept = conceptService.getConceptByUuid(rootDiagnosisSetUuid);
//        if (rootDiagnosisConcept == null) {
//            logger.info(String.format("Configuration required:%s. Not grouping diagnosis",GP_DIAGNOSIS_SET_OF_SETS));
//            //throw new IllegalStateException("Configuration required: " + EmrApiConstants.GP_DIAGNOSIS_SET_OF_SETS);
//            return;
//        }
//        List<Concept> diagnosesCategories = rootDiagnosisConcept.getSetMembers();
//        Concept diagnosisSet = null;
//        for (Concept conceptSet : diagnosesCategories) {
//            if (conceptSet.getName().getName().equalsIgnoreCase(UNCATEGORIZED_DIAGNOSES_NAME)) {
//                diagnosisSet = conceptSet;
//                break;
//            }
//        }
//        if ((diagnosisSet == null) || !diagnosisSet.isSet()) {
//            logger.info("Configuration required: Could not find uncategorized diagnosis ConvSet.");
//            //throw new IllegalStateException("Configuration required: " + EmrApiConstants.GP_DIAGNOSIS_SET_OF_SETS);
//            return;
//        }

        Concept diagnosisSet = conceptService.getConceptByName(UNCATEGORIZED_DIAGNOSES_NAME);
        if (diagnosisSet == null) {
            logger.info("Configuration required: Could not find ConvSet - uncategorized diagnoses");
            //throw new IllegalStateException("Configuration required: uncategorized diagnoses");
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

    protected String getGlobalProperty(String globalPropertyName, boolean required) {
        String globalProperty = administrationService.getGlobalProperty(globalPropertyName);
        if (required && StringUtils.isEmpty(globalProperty)) {
            throw new IllegalStateException("Configuration required: " + globalPropertyName);
        }
        return globalProperty;
    }
}
