package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import org.bahmni.module.terminology.application.model.IdMapping;

import static org.bahmni.module.terminology.application.model.TerminologyClientConstants.CONCEPT;

import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.bahmni.module.terminology.application.model.TerminologyClientConstants.CONCEPT_REFERENCE_TERM;

import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.Locale.ENGLISH;
import static org.bahmni.module.terminology.util.FileUtil.asString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class ConceptEventWorkerIntegrationTest extends BaseModuleWebContextSensitiveTest {

    public static final String TR_CONCEPT_URL = "www.bdshr-tr.com/openmrs/ws/rest/v1/tr/concepts/";
    public static final String TR_REFTERM_URL = "www.bdshr-tr.com/openmrs/ws/rest/v1/tr/referenceterms/";
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9997);

    @Autowired
    private AuthenticatedHttpClient httpClient;
    @Autowired
    private TRFeedProperties trFeedProperties;
    @Autowired
    private ConceptSyncService ConceptSyncService;
    @Autowired
    private ConceptRequestMapper conceptMapper;
    @Autowired
    private IdMappingsRepository idMappingsRepository;
    @Autowired
    private ConceptService conceptService;

    @Before
    public void setUp() {

    }

    /**
     * Built in OpenMRS concept classes: Test,Procedure,Drug,Diagnosis,Finding,Anatomy,Question,
     * LabSet,MedSet,ConvSet,Misc,Symptom,Symptom/Finding,Specimen,Misc Order,Program,Workflow,State
     * <p/>
     * Built in OpenMRS concept datatypes: Numeric,Coded,Text,N/A,Document,Date,Time,Datetime,
     * Boolean,Rule,Structured Numeric,Complex
     */

    @Test
    public void shouldSyncConcepts() {
        String externalId = "216c8246-202c-4376-bfa8-3278d1049630";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/concepts/" + externalId;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/concept.json"))));
        ConceptEventWorker worker = new ConceptEventWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper);

        worker.process(new Event("eventId", concept_event_url, "title", "feedUri", null));

        Concept concept = Context.getConceptService().getConceptByName("tbtest");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getVersion(), is("1.1.1"));
        assertThat(concept.getName().getName(), is("tbtest"));
        assertThat(concept.getDatatype().getName(), is("Text"));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
        assertThat(concept.isSet(), is(false));
        assertThat(concept.isRetired(), is(false));
        assertIdMapping(concept.getUuid(), externalId, CONCEPT,
                "http://172.18.46.53:9080" + concept_event_url);
        assertFullySpecifiedName(concept.getFullySpecifiedName(ENGLISH), "tbtest");
        assertConceptNames(concept.getNames());
        assertDescription(concept.getDescription(), "description123");
        //assertReferenceTerms(concept.getConceptMappings(), bloodPressureConcept.getConceptMappings());
    }


    @Test
    public void shouldSyncConceptsWithNonExistentClass() {
        String externalId = "216c8246-202c-4376-bfa8-3278d1049631";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/concepts/" + externalId;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/concept_with_new_class.json"))));
        ConceptEventWorker worker = new ConceptEventWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri", null));
        Concept concept = Context.getConceptService().getConceptByName("tbtest");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getName().getName(), is("tbtest"));
        assertThat(concept.getDatatype().getName(), is("Text"));
        assertThat(concept.getConceptClass().getName(), is("TestClass"));
        assertIdMapping(concept.getUuid(), externalId, CONCEPT,
                "http://172.18.46.53:9080" + concept_event_url);
        assertFullySpecifiedName(concept.getFullySpecifiedName(ENGLISH), "tbtest");
    }

    @Test
    public void shouldSyncConceptSet() {
        createAndAssertSystolicConcept();
        createAndAssertDiastolicConcept();
        String bpExternalUuid = "444c8246-202c-4376-bfa8-3278d1049444";
        syncConcept(bpExternalUuid, "concept_bp.json");
        Concept bloodPressureConcept = conceptService.getConceptByName("BloodPressure");
        assertNotNull("Blood Pressure concept should have been created", bloodPressureConcept);
        assertIdMapping(bloodPressureConcept.getUuid(), bpExternalUuid,
                CONCEPT, TR_CONCEPT_URL + bpExternalUuid);

        ImmutableMap<String, String> bloodPressureRefTerm =
                ImmutableMap.of("code", "ICD101",
                        "source", "ICD10",
                        "mapType", "SAME-AS",
                        "externalId", "565496c2-bdfe-4cce-87d4-92091ab1f67a");
        List<ImmutableMap<String, String>> refTermsActual = Arrays.asList(bloodPressureRefTerm);
        assertReferenceTerms(refTermsActual, bloodPressureConcept.getConceptMappings());
    }

    @Test
    public void shouldUpdateMembersOfSet() {
        syncConcept("96ff1fb3-301e-4483-ae52-172aaf743b22", "sets/coded_answer_1.json");
        Concept coded_answer_1 = conceptService.getConceptByName("Test Concept 1");
        assertNotNull("First set member should have been created", coded_answer_1);

        syncConcept("4620829f-88c8-448f-83d4-5aca37d1de5e", "sets/coded_answer_2.json");
        Concept coded_answer_2 = conceptService.getConceptByName("Test Concept 2");
        assertNotNull("Second set member should have been created", coded_answer_2);

        syncConcept("57c7e65a-3744-4db8-98e1-e7d4c3cb24bf", "sets/set_with_2_members.json");
        syncConcept("57c7e65a-3744-4db8-98e1-e7d4c3cb24bf", "sets/set_with_1_member.json");
        Concept coaded_concept = conceptService.getConceptByName("A Coaded Concept");
        assertNotNull("Set Concept should have been Updated", coaded_concept);
        assertEquals(1, coaded_concept.getSetMembers().size());
    }

    @Test
    public void shouldUpdateConceptAnswersOfValueSet() {
        syncConcept("96ff1fb3-301e-4483-ae52-172aaf743b22", "sets/coded_answer_1.json");
        Concept coded_answer_1 = conceptService.getConceptByName("Test Concept 1");
        assertNotNull("First Coded Answer should have been created", coded_answer_1);

        syncConcept("4620829f-88c8-448f-83d4-5aca37d1de5e", "sets/coded_answer_2.json");
        Concept coded_answer_2 = conceptService.getConceptByName("Test Concept 2");
        assertNotNull("Second Coded Answer should have been created", coded_answer_2);

        syncConcept("c95aa4c3-8ad7-4632-a7ee-3889d11e6b8e", "sets/valueset_with_2_codes.json");
        syncConcept("c95aa4c3-8ad7-4632-a7ee-3889d11e6b8e", "sets/valueset_with_1_code.json");
        Concept test_value_set_1 = conceptService.getConceptByName("Test Value Set");

        final IdMapping idMapping = idMappingsRepository.findByExternalId("c95aa4c3-8ad7-4632-a7ee-3889d11e6b8e");
        Concept conceptByUuid = conceptService.getConceptByUuid(idMapping.getInternalId());

        assertNotNull("Test Value Set should have been Updated", test_value_set_1);
        assertEquals(1, test_value_set_1.getAnswers().size());
    }

    @Test
    public void shouldSyncCodedConcept() {

        syncConcept("666c8246-202c-4376-bfa8-3278d1049666", "concept_male.json");
        Concept maleSexConcept = conceptService.getConceptByName("Male Sex");
        assertNotNull("Male sex concept should have been created", maleSexConcept);

        syncConcept("777c8246-202c-4376-bfa8-3278d1049777", "concept_female.json");
        Concept femaleSexConcept = conceptService.getConceptByName("Female Sex");
        assertNotNull("Female Sex Concept should have been created", femaleSexConcept);

        syncConcept("888c8246-202c-4376-bfa8-3278d1049888", "concept_patient_sex.json");

        Concept patientSexConcept = conceptService.getConceptByName("Patient Sex");
        assertNotNull("Patient Sex Concept should have been created", patientSexConcept);


        assertConceptAnswers(patientSexConcept, Arrays.asList(maleSexConcept, femaleSexConcept));


    }

    private void assertConceptAnswers(Concept patientSexConcept, List<Concept> answerConcepts) {
        assertEquals(answerConcepts.size(), patientSexConcept.getAnswers().size());
        for (Concept answer : answerConcepts) {
            boolean existsAsAnswer = false;
            for (ConceptAnswer conceptAnswer : patientSexConcept.getAnswers()) {
                if (conceptAnswer.getAnswerConcept().getUuid().equals(answer.getUuid())) {
                    existsAsAnswer = true;
                    break;
                }
            }
            assertTrue(String.format("Answer '%s' should exist in Concept", answer.getName().getName()), existsAsAnswer);

        }

    }

    private void createAndAssertDiastolicConcept() {
        syncConcept("222c8246-202c-4376-bfa8-3278d1042222", "concept_diastolic.json");
        Concept concept = conceptService.getConceptByName("Diastolic");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getVersion(), is("1.1"));
        assertThat(concept.getName().getName(), is("Diastolic"));
        assertThat(concept.getDatatype().getName(), is("Numeric"));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
        assertThat(concept.isSet(), is(false));
        assertThat(concept.isRetired(), is(false));
        assertFullySpecifiedName(concept.getFullySpecifiedName(ENGLISH), "Diastolic");
        assertConceptNames(concept.getNames());
        assertDescription(concept.getDescription(), "Diastolic Blood Pressure");
    }

    private void createAndAssertSystolicConcept() {
        syncConcept("216c8246-202c-4376-bfa8-3278d1049630", "concept_systolic.json");
        Concept concept = conceptService.getConceptByName("Systolic");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getVersion(), is("1.1"));
        assertThat(concept.getName().getName(), is("Systolic"));
        assertThat(concept.getDatatype().getName(), is("Numeric"));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
        assertThat(concept.isSet(), is(false));
        assertThat(concept.isRetired(), is(false));
        assertFullySpecifiedName(concept.getFullySpecifiedName(ENGLISH), "Systolic");
        assertConceptNames(concept.getNames());
        assertDescription(concept.getDescription(), "Systolic Blood Pressure");
    }

    private void syncConcept(String externalUuid, String resName) {
        String concept_feed_url_prefix = "/openmrs/ws/rest/v1/tr/concepts/";
        givenThat(get(urlEqualTo(concept_feed_url_prefix + externalUuid))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/" + resName))));
        ConceptEventWorker worker = new ConceptEventWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper);
        worker.process(new Event("eventId", concept_feed_url_prefix + externalUuid, "title", "feedUri", null));
    }

    private void assertFullySpecifiedName(ConceptName fullySpecifiedName, String name) {
        assertNotNull(fullySpecifiedName);
        assertThat(fullySpecifiedName.getName(), is(name));
        assertThat(fullySpecifiedName.getConceptNameType().name(), is("FULLY_SPECIFIED"));
    }

    private void assertDescription(ConceptDescription conceptDescription, String descr) {
        assertNotNull(conceptDescription);
        assertThat(conceptDescription.getDescription(), is(descr));
        assertThat(conceptDescription.getLocale(), is(ENGLISH));
    }

    private void assertConceptNames(Collection<ConceptName> names) {
        assertNotNull(names);
        assertThat(names.size(), is(4));
        for (ConceptName name : names) {
            assertNotNull(name.getName());
            assertThat(name.getLocale(), is(ENGLISH));
            if (name.getConceptNameType() != null) {
                assertTrue(Arrays.asList("INDEX_TERM", "SHORT", "FULLY_SPECIFIED").contains(name.getConceptNameType().name()));
            }
        }
    }

    private void assertReferenceTerms(List<ImmutableMap<String, String>> expectedResults, Collection<ConceptMap> actualResults) {
        if (expectedResults.size() > 0) {
            assertNotNull(actualResults);
            assertTrue(actualResults.iterator().hasNext());
        }
        for (ImmutableMap<String, String> expected : expectedResults) {
            ConceptMap actualConceptMap = null;
            for (ConceptMap actualResult : actualResults) {
                if (actualResult.getConceptReferenceTerm().getCode().equals(expected.get("code"))) {
                    actualConceptMap = actualResult;
                    break;
                }
            }
            assertNotNull("Could not find expected term:", actualConceptMap);
            assertEquals(actualConceptMap.getConceptMapType().getName(), expected.get("mapType").toLowerCase());
            ConceptReferenceTerm term = actualConceptMap.getConceptReferenceTerm();
            ConceptSource conceptSource = term.getConceptSource();
            assertThat(conceptSource.getName(), is(expected.get("source")));
            String expectedExternalId = expected.get("externalId");
            assertIdMapping(term.getUuid(), expectedExternalId, CONCEPT_REFERENCE_TERM,
                    TR_REFTERM_URL + expectedExternalId);
        }

    }

    private void assertIdMapping(String internalId, String externalId, String type, String uri) {
        final IdMapping idMapping = idMappingsRepository.findByExternalId(externalId);
        assertNotNull(idMapping);
        assertEquals(internalId, idMapping.getInternalId());
        assertEquals(type, idMapping.getType());
        assertEquals(uri, idMapping.getUri());
    }
}