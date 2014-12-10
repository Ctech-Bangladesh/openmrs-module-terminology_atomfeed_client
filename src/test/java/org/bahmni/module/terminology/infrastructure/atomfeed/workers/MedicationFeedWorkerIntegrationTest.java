package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import org.bahmni.module.terminology.application.model.drug.Medication;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.bahmni.module.terminology.util.FileUtil;
import org.codehaus.jackson.map.ObjectMapper;
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
                        .withBody(asString("stubdata/medication.json"))));

        MedicationFeedWorker worker = new MedicationFeedWorker(trFeedProperties, httpClient, conceptService, idMappingsRepository);
        worker.process(new Event("eventId", concept_event_url, "title", "feedUri"));


        Drug drug = conceptService.getDrugByUuid(idMappingsRepository.findByExternalId(uuid).getInternalId());
        assertNotNull(drug);
        assertEquals("Crocin 500mg", drug.getName());
        assertEquals("500 mg", drug.getStrength());
        assertEquals("Paracetamol", drug.getConcept().getName().getName());
        assertEquals("Tablet", drug.getDosageForm().getName().getName());
    }
}