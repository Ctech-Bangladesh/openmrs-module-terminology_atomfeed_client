package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.hamcrest.CoreMatchers;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.bahmni.module.terminology.util.FileUtil.asString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class ConceptFeedWorkerIntegrationTest extends BaseModuleWebContextSensitiveTest {

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

    @Test
    public void shouldSyncConcepts() {
        givenThat(get(urlEqualTo("/openmrs/ws/rest/v1/concept/ec0f4153-3f7f-446a-b82d-7756f0fdcac1?v=full"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/concept.json"))));
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper, ConceptType.Diagnosis);

        worker.process(new Event("eventId", "/openmrs/ws/rest/v1/concept/ec0f4153-3f7f-446a-b82d-7756f0fdcac1?v=full", "title", "feedUri"));

        Concept concept = Context.getService(ConceptService.class).getConceptByName("tbtest");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getName().getName(), is("tbtest"));
        assertThat(concept.getDatatype().getName(), is("Text"));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
    }

    @Test
    public void shouldSyncDiagnosis() {
        givenThat(get(urlEqualTo("/openmrs/ws/rest/v1/concept/cebe4ed6-3f86-49a6-98fd-46c01e40a771?v=full"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/diagnosis.json"))));
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper, ConceptType.Diagnosis);

        worker.process(new Event("eventId", "/openmrs/ws/rest/v1/concept/cebe4ed6-3f86-49a6-98fd-46c01e40a771?v=full", "title", "feedUri"));

        Concept concept = Context.getService(ConceptService.class).getConceptByName("samplediagnosis");
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getName().getName(), is("samplediagnosis"));
        assertThat(concept.getDatatype().getName(), is("Text"));
        assertThat(concept.getConceptClass(), is(CoreMatchers.notNullValue()));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
    }
}