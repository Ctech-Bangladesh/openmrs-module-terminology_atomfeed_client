package org.openmrs.integration;


import org.junit.Before;
import org.junit.Test;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;

import static org.junit.Assert.assertNotNull;
import static org.openmrs.integration.ConceptReferenceData.concept;
import static org.openmrs.integration.ConceptReferenceData.conceptSource;
import static org.openmrs.integration.ConceptReferenceData.referenceTerm;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class ConceptAPITest extends BaseModuleWebContextSensitiveTest {


    private static final String SOURCE_NAME = "ICD10 SOURCE";
    private static final String SOURCE_CODE = "ICD10";
    private static final String CONCEPT_NAME = "CHOLERA";
    private static final String REFERENCE_CODE = "A00.1";

    @Before
    public void setUp() {
        Context.getService(ConceptService.class).saveConceptSource(conceptSource(SOURCE_CODE, SOURCE_NAME));
    }

    @Test
    public void shouldInitializeConceptService() {
        ConceptService service = Context.getService(ConceptService.class);
        System.out.println("concept service:" + service);
    }

    @Test
    public void shouldCreateAConcept() {
        ConceptService service = Context.getService(ConceptService.class);

        service.saveConcept(concept(CONCEPT_NAME));

        assertNotNull(service.getConceptByName(CONCEPT_NAME));
    }


    @Test
    public void shouldCreateConceptTerm() throws Exception {
        ConceptService service = Context.getService(ConceptService.class);
        ConceptSource conceptSource = service.getConceptSourceByName(SOURCE_NAME);
        ConceptReferenceTerm referenceTerm = referenceTerm(REFERENCE_CODE, conceptSource);

        service.saveConceptReferenceTerm(referenceTerm);

        ConceptReferenceTerm savedReferenceTerm = service.getConceptReferenceTermByCode(REFERENCE_CODE, conceptSource);
        assertNotNull(savedReferenceTerm);

    }
}
