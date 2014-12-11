package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.bahmni.module.terminology.util.FileUtil.asString;
import static org.junit.Assert.*;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class MedicationFeedWorkerIntegrationTest extends BaseModuleWebContextSensitiveTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9997);

    @Autowired
    private AuthenticatedHttpClient httpClient;
    @Autowired
    private TRFeedProperties trFeedProperties;
    @Autowired
    private ConceptService conceptService;
    @Autowired
    IdMappingsRepository idMappingsRepository;

    @Test
    public void shouldSyncTheDrug() throws Exception {
        executeDataSet("stubdata/medication_concept_mapping.xml");

        String uuid = "ada47e63-5988-4f51-8282-e22fb66a7332";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/medications/" + uuid;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/medication/medication.json"))));

        MedicationFeedWorker worker = new MedicationFeedWorker(trFeedProperties, httpClient, conceptService, idMappingsRepository);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri"));


        Drug drug = conceptService.getDrugByUuid(idMappingsRepository.findByExternalId(uuid).getInternalId());
        assertNotNull(drug);
        assertEquals("Crocin 500mg", drug.getName());
        assertEquals("500 mg", drug.getStrength());
        assertEquals("Paracetamol", drug.getConcept().getName().getName());
        assertEquals("Tablet", drug.getDosageForm().getName().getName());
        assertFalse("Drug should not be retired", drug.getRetired());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowErrorIfMedicationDoesNotHaveLocalConcept() throws Exception {
        executeDataSet("stubdata/medication_concept_mapping.xml");

        String uuid = "ada47e63-5988-4f51-8282-e22fb66a7332";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/medications/" + uuid;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/medication/medication_without_concept.json"))));

        MedicationFeedWorker worker = new MedicationFeedWorker(trFeedProperties, httpClient, conceptService, idMappingsRepository);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri"));
    }

    @Test
    public void shouldSyncTheDrugWithoutFormConceptWhenFormConceptNotFoundLocally() throws Exception {
        executeDataSet("stubdata/medication_concept_mapping.xml");

        String uuid = "04558a65-0e18-4fc4-bb8d-4f9a89fbec5c";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/medications/" + uuid;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/medication/medication_without_form_concept.json"))));

        MedicationFeedWorker worker = new MedicationFeedWorker(trFeedProperties, httpClient, conceptService, idMappingsRepository);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri"));


        Drug drug = conceptService.getDrugByUuid(idMappingsRepository.findByExternalId(uuid).getInternalId());
        assertNotNull(drug);
        assertEquals("Benedryl", drug.getName());
        assertEquals("20.0", drug.getStrength());
        assertEquals("diphenhydramine", drug.getConcept().getName().getName());
        assertNull(drug.getDosageForm());
    }

    @Test
    public void shouldUpdateDetailsOfAlreadyExistingDrug() throws Exception {
        executeDataSet("stubdata/medication_concept_mapping.xml");

        String uuid = "ada47e63-5988-4f51-8282-e22fb66a7332";
        String concept_event_url = "/openmrs/ws/rest/v1/tr/medications/" + uuid;

        givenThat(get(urlEqualTo(concept_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/medication/medication.json"))));

        Drug drug = conceptService.getDrugByUuid(idMappingsRepository.findByExternalId(uuid).getInternalId());
        assertNotNull(drug);
        assertEquals("A drug", drug.getName());
        assertEquals("10mg", drug.getStrength());
        assertEquals("diphenhydramine", drug.getConcept().getName().getName());
        assertNull(drug.getDosageForm());

        MedicationFeedWorker worker = new MedicationFeedWorker(trFeedProperties, httpClient, conceptService, idMappingsRepository);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri"));


        assertNotNull(drug);
        assertEquals("Crocin 500mg", drug.getName());
        assertEquals("500 mg", drug.getStrength());
        assertEquals("Paracetamol", drug.getConcept().getName().getName());
        assertEquals("Tablet", drug.getDosageForm().getName().getName());
        assertFalse("Drug should not be retired", drug.getRetired());
    }
}