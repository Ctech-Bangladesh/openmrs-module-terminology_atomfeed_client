package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.bahmni.module.terminology.application.service.SHReferenceTermService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptReferenceTermRequestMapper;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptReferenceTermMap;
import org.openmrs.api.context.Context;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.bahmni.module.terminology.util.FileUtil.asString;
import static org.junit.Assert.*;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class ConceptReferenceTermFeedWorkerIntegrationTest extends BaseModuleWebContextSensitiveTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9997);

    @Autowired
    private AuthenticatedHttpClient httpClient;
    @Autowired
    private TRFeedProperties trFeedProperties;
    @Autowired
    private SHReferenceTermService referenceTermService;
    @Autowired
    private ConceptReferenceTermRequestMapper referenceTermRequestMapper;
    @Autowired
    IdMappingsRepository idMappingsRepository;

    private ConceptReferenceTermFeedWorker referenceTermFeedWorker;

    @Before
    public void setup() {
        referenceTermFeedWorker = new ConceptReferenceTermFeedWorker(httpClient, trFeedProperties, referenceTermService, referenceTermRequestMapper);
    }

    @Test
    public void shouldSyncAReferenceTerm() throws Exception {

        executeDataSet("stubdata/reference_term_mapping.xml");

        String external_id = "df2d10af-z2w4-49fe-951d-46f614ff6100";
        String reference_term_event_url = "/openmrs/ws/rest/v1/tr/referenceterms/" + external_id;

        givenThat(get(urlEqualTo(reference_term_event_url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/reference_term.json"))));

        referenceTermFeedWorker.process(new Event("eventId", reference_term_event_url, "title", "feedUri"));
        ConceptReferenceTerm referenceTerm = Context.getConceptService().getConceptReferenceTermByUuid(idMappingsRepository.findByExternalId(external_id).getInternalId());
        assertNotNull(referenceTerm);
        assertEquals("Paracetamol 1",referenceTerm.getName());
        assertEquals("N02BE02",referenceTerm.getCode());
        assertFalse(referenceTerm.getRetired());

        Set<ConceptReferenceTermMap> conceptReferenceTermMaps = referenceTerm.getConceptReferenceTermMaps();
        for (ConceptReferenceTermMap conceptReferenceTermMap : conceptReferenceTermMaps) {
            assertEquals("Paracetamol 1",conceptReferenceTermMap.getTermA().getName());
            assertEquals("Paracetamol",conceptReferenceTermMap.getTermB().getName());
            assertEquals("same-as",conceptReferenceTermMap.getConceptMapType().getName());
        }

    }
}