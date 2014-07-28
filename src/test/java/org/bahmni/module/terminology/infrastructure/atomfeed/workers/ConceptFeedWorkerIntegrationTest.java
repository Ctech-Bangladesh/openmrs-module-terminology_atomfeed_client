package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.hamcrest.CoreMatchers;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.bahmni.module.terminology.util.FileUtil.asString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

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
        givenThat(get(urlEqualTo("/openmrs/ws/rest/v1/tr/concepts/216c8246-202c-4376-bfa8-3278d1049630"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(asString("stubdata/concept.json"))));
        ConceptFeedWorker worker = new ConceptFeedWorker(httpClient, trFeedProperties, ConceptSyncService, conceptMapper);

        worker.process(new Event("eventId", "/openmrs/ws/rest/v1/tr/concepts/216c8246-202c-4376-bfa8-3278d1049630", "title", "feedUri"));

        Concept concept = Context.getService(ConceptService.class).getConceptByName("tbtest");
        assertBasicConcept(concept);
        assertFullySpecifiedName(concept.getFullySpecifiedName(Locale.ENGLISH));
        assertConceptNames(concept.getNames());
        assertDescription(concept.getDescription());
        assertReferenceTerms(concept.getConceptMappings());
    }

    private void assertBasicConcept(Concept concept) {
        assertThat(concept, is(notNullValue()));
        assertThat(concept.getVersion(), is("1.1.1"));
        assertThat(concept.getName().getName(), is("tbtest"));
        assertThat(concept.getDatatype().getName(), is("Text"));
        assertThat(concept.getConceptClass().getName(), is("Diagnosis"));
        assertThat(concept.isSet(), is(false));
    }

    private void assertFullySpecifiedName(ConceptName fullySpecifiedName) {
        assertNotNull(fullySpecifiedName);
        assertThat(fullySpecifiedName.getName(), is("tbtest"));
        assertThat(fullySpecifiedName.getConceptNameType().name(), is("FULLY_SPECIFIED"));
    }

    private void assertDescription(ConceptDescription description) {
        assertNotNull(description);
        assertThat(description.getDescription(), is("description123"));
    }

    private void assertConceptNames(Collection<ConceptName> names) {
        assertNotNull(names);
        assertThat(names.size(), is(4));
        for (ConceptName name : names) {
            assertNotNull(name.getName());
            assertThat(name.getLocale(), is(Locale.ENGLISH));
            if (name.getConceptNameType() != null) {
                assertTrue(Arrays.asList("INDEX_TERM", "SHORT", "FULLY_SPECIFIED").contains(name.getConceptNameType().name()));
            }
        }
    }

    private void assertReferenceTerms(Collection<ConceptMap> conceptMappings) {
        assertNotNull(conceptMappings);
        assertTrue(conceptMappings.iterator().hasNext());

        final ConceptMap conceptMap = conceptMappings.iterator().next();
        assertNotNull(conceptMap);
        assertThat(conceptMap.getConceptMapType().getName(), is("same-as"));

        final ConceptReferenceTerm conceptReferenceTerm = conceptMap.getConceptReferenceTerm();
        assertNotNull(conceptReferenceTerm);
        assertThat(conceptReferenceTerm.getName(), is("ICD101"));
        assertThat(conceptReferenceTerm.getCode(), is("ICD101"));

        final ConceptSource conceptSource = conceptReferenceTerm.getConceptSource();
        assertNotNull(conceptSource);
        assertThat(conceptSource.getName(), is("ICD10"));
        assertThat(conceptSource.getHl7Code(), is("ICD10"));
    }
}