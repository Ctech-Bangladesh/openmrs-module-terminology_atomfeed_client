package org.openmrs.integration;


import org.bahmni.module.terminology.application.postprocessor.PostProcessorFactory;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ChiefComplaintPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ConceptPostProcessor;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class PostProcessorFactoryIT extends BaseModuleWebContextSensitiveTest {

    @Autowired
    PostProcessorFactory postProcessorFactory;

    @Test
    public void shouldMapDiagnosisProcessors() throws Exception {
        Concept diagnosisConcept = getConceptForClass("diagnosis");
        ConceptPostProcessor postProcessor = postProcessorFactory.getPostProcessor(diagnosisConcept);

        assertTrue(postProcessor instanceof DiagnosisPostProcessor);

    }

    @Test
    public void shouldMapChiefComplaintProcessors() throws Exception {
        Concept chiefComplaintConcept = getConceptForClass("finding");
        ConceptPostProcessor postProcessor = postProcessorFactory.getPostProcessor(chiefComplaintConcept);

        assertTrue(postProcessor instanceof ChiefComplaintPostProcessor);

        chiefComplaintConcept = getConceptForClass("symptom");
        postProcessor = postProcessorFactory.getPostProcessor(chiefComplaintConcept);

        assertTrue(postProcessor instanceof ChiefComplaintPostProcessor);

        chiefComplaintConcept = getConceptForClass("symptom/finding");
        postProcessor = postProcessorFactory.getPostProcessor(chiefComplaintConcept);

        assertTrue(postProcessor instanceof ChiefComplaintPostProcessor);

    }

    private Concept getConceptForClass(String conceptClassName) {
        Concept chiefComplaintConcept = new Concept();
        ConceptClass conceptClass = new ConceptClass();
        conceptClass.setName(conceptClassName);
        chiefComplaintConcept.setConceptClass(conceptClass);
        return chiefComplaintConcept;
    }
}

