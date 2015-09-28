package org.openmrs.integration;


import junitx.framework.ListAssert;
import org.apache.commons.collections.Transformer;
import org.bahmni.module.terminology.application.postprocessor.PostProcessorFactory;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.*;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.CollectionUtils.collect;
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

        assertEquals(3, postProcessors.size());

        assertPostProcessorTypes(postProcessors, asList(CleanUpChiefComplaintPostProcessor.class.getName(),
                DiagnosisAsAnswerPostProcessor.class.getName(), DiagnosisAsSetMemberPostProcessor.class.getName()));

    }

    @Test
    public void shouldMapProcedureProcessors() throws Exception {
        Concept procedureConcept = getConceptForClass("Procedure");
        List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(procedureConcept);
        assertEquals(2, postProcessors.size());
        assertPostProcessorTypes(postProcessors, asList(CleanUpChiefComplaintPostProcessor.class.getName(), ProcedurePostProcessor.class.getName()));
    }

    @Test
    public void shouldMapChiefComplaintProcessors() throws Exception {
        Concept chiefComplaintConcept = getConceptForClass("finding");

        List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(1, postProcessors.size());
        assertTrue(postProcessors.get(0) instanceof CleanUpChiefComplaintPostProcessor);

        chiefComplaintConcept = getConceptForClass("symptom");
        postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(2, postProcessors.size());
        assertPostProcessorTypes(postProcessors, asList(ChiefComplaintPostProcessor.class.getName(),
                CleanUpChiefComplaintPostProcessor.class.getName()));

        chiefComplaintConcept = getConceptForClass("symptom/finding");
        postProcessors = postProcessorFactory.getPostProcessors(chiefComplaintConcept);
        assertEquals(2, postProcessors.size());
        assertPostProcessorTypes(postProcessors, asList(ChiefComplaintPostProcessor.class.getName(),
                CleanUpChiefComplaintPostProcessor.class.getName()));
    }

    private Concept getConceptForClass(String conceptClassName) {
        Concept chiefComplaintConcept = new Concept();
        ConceptClass conceptClass = new ConceptClass();
        conceptClass.setName(conceptClassName);
        chiefComplaintConcept.setConceptClass(conceptClass);
        return chiefComplaintConcept;
    }

    private void assertPostProcessorTypes(List<ConceptPostProcessor> postProcessors, List<String> expected) {
        Collection names = collect(postProcessors, new Transformer() {
            @Override
            public String transform(Object o) {
                return o.getClass().getName();
            }
        });
        ListAssert.assertEquals(asList(expected.toArray()), asList(names.toArray()));
    }
}

