package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.bahmni.module.terminology.application.model.ConceptNameRequest;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.application.service.ConceptSyncService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptRequestMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConceptFeedWorkerTest {

    public static final String CONCEPT_BASE_URL = "http://localhost/";

    @Mock
    private ConceptSyncService ConceptSyncService;

    @Mock
    private ConceptRequestMapper mapper;

    @Mock
    private AuthenticatedHttpClient httpClient;

    @Mock
    private TRFeedProperties properties;

    private ConceptFeedWorker conceptFeedWorker;

    private Event event;

    @Before
    public void setup() {
        initMocks(this);
        event = new Event("eventId", "/content", "title", "feedUri");
        properties = createProperties();
        conceptFeedWorker = new ConceptFeedWorker(httpClient, properties, ConceptSyncService, mapper, ConceptType.Diagnosis);
    }

    private TRFeedProperties createProperties() {
        Properties feedDefaults = new Properties();
        feedDefaults.setProperty(TRFeedProperties.TERMINOLOGY_FEED_URI, CONCEPT_BASE_URL);
        return new TRFeedProperties(feedDefaults);
    }

    @Test
    public void shouldSaveTheConceptFetched() throws IOException {
        HashMap<String, Object> response = new HashMap<>();

        ConceptRequest concept = new ConceptRequest();
        ConceptNameRequest name = new ConceptNameRequest();
        name.setConceptNameType("concept");
        name.setLocale(Locale.CANADA.getDisplayName());
        concept.setFullySpecifiedName(name);

        when(mapper.map(response)).thenReturn(concept);
        when(httpClient.get("http://localhost/content", HashMap.class)).thenReturn(response);

        conceptFeedWorker.process(event);

        verify(ConceptSyncService, times(1)).sync(concept, ConceptType.Diagnosis);
    }
}