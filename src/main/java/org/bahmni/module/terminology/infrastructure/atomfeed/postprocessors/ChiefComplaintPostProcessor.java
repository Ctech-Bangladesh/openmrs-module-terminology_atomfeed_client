package org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("chiefComplaintPostProcessor")
public class ChiefComplaintPostProcessor implements ConceptPostProcessor {

    @Autowired
    ConceptService conceptService;

    @Autowired
    @Qualifier("adminService")
    protected AdministrationService administrationService;

    public static final String CHIEF_COMPLAINT_CONCEPT_NAME = "Chief Complaint";

    private final Logger logger = Logger.getLogger(ChiefComplaintPostProcessor.class);

    @Override
    public void process(Concept concept) {
        Concept chiefComplaintConcept = conceptService.getConceptByName(CHIEF_COMPLAINT_CONCEPT_NAME);
        if (chiefComplaintConcept == null) {
            logger.info("Configuration required: Could not find concept - Chief Complaint");
            return;
        }

        if (!isPresentAsMember(chiefComplaintConcept, concept)) {
            ConceptAnswer answer = new ConceptAnswer();
            answer.setAnswerConcept(concept);
            chiefComplaintConcept.addAnswer(answer);
            conceptService.saveConcept(chiefComplaintConcept);
        } else {
            logger.info("Concept is already a member of existing " + CHIEF_COMPLAINT_CONCEPT_NAME);
        }

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
