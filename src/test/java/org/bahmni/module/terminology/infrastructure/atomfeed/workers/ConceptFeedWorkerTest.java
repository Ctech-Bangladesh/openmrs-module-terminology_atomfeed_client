package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.bahmni.module.terminology.application.mappers.ConceptMapper;
import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConceptFeedWorkerTest {

    public static final String CONCEPT_BASE_URL = "http://localhost/";

    private ArgumentCaptor<Map> argumentCaptor = ArgumentCaptor.forClass(Map.class);

    @Mock
    private ConceptRestService conceptService;

    @Mock
    private ConceptMapper mapper;

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
        conceptFeedWorker = new ConceptFeedWorker(httpClient, properties, conceptService);
    }

    private TRFeedProperties createProperties() {
        Properties feedDefaults = new Properties();
        feedDefaults.setProperty(TRFeedProperties.TERMINOLOGY_FEED_URI, CONCEPT_BASE_URL);
        return new TRFeedProperties(feedDefaults);
    }

    @Test
    public void shouldSaveTheConceptFetched() throws IOException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        when(httpClient.get("http://localhost/content", HashMap.class)).thenReturn(response);

        conceptFeedWorker.process(event);

        verify(conceptService, times(1)).save(argumentCaptor.capture());
        assertEquals(response, argumentCaptor.getValue());
    }
}