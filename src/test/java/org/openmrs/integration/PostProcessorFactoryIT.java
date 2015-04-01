package org.openmrs.integration;


import org.bahmni.module.terminology.application.postprocessor.PostProcessorFactory;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.*;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class PostProcessorFactoryIT extends BaseModuleWebContextSensitiveTest {

    @Autowired
    PostProcessorFactory postProcessorFactory;

    @Test
    public void shouldMapDiagnosisProcessors() throws Exception {
        Concept diagnosisConcept = getConceptForClass("diagnosis");
        List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(diagnosisConcept);

        assertEquals(postProcessors.size(), 2);
        assertTrue(postProcessors.get(0) instanceof DiagnosisAsAnswerPostProcessor);
        assertTrue(postProcessors.get(1) instanceof DiagnosisAsSetMemberPostProcessor);

    }

    @Test
    public void shouldMapProcedureProcessors() throws Exception {
        Concept procedureConcept = getConceptForClass("Procedure");
        List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(procedureConcept);

        assertEquals(postProcessors.size(), 1);
        assertTrue(postProcessors.get(0) instanceof ProcedurePostProcessor);

    }

    @Test
    public void shouldMapChiefComplaintProcessors() throws Exception {
        Concept chiefComplaintConcept = getConceptForClass("finding");

        List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(postProcessors.size(), 1);
        assertTrue(postProcessors.get(0) instanceof ChiefComplaintPostProcessor);

        chiefComplaintConcept = getConceptForClass("symptom");
        postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(postProcessors.size(), 1);
        assertTrue(postProcessors.get(0) instanceof ChiefComplaintPostProcessor);

        chiefComplaintConcept = getConceptForClass("symptom/finding");
        postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(postProcessors.size(), 1);
        assertTrue(postProcessors.get(0) instanceof ChiefComplaintPostProcessor);

    }

    private Concept getConceptForClass(String conceptClassName) {
        Concept chiefComplaintConcept = new Concept();
        ConceptClass conceptClass = new ConceptClass();
        conceptClass.setName(conceptClassName);
        chiefComplaintConcept.setConceptClass(conceptClass);
        return chiefComplaintConcept;
    }
}

