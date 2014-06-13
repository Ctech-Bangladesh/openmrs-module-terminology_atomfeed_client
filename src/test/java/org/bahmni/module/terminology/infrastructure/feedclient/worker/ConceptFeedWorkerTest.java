package org.bahmni.module.terminology.infrastructure.feedclient.worker;

import org.bahmni.module.terminology.application.service.ConceptRestService;
import org.bahmni.module.terminology.infrastructure.http.ConceptHttpClient;
import org.bahmni.module.terminology.infrastructure.model.TRFeedProperties;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConceptFeedWorkerTest {

    public static final String CONCEPT_BASE_URL = "http://localhost/";

    private ArgumentCaptor<SimpleObject> argumentCaptor = ArgumentCaptor.forClass(SimpleObject.class);

    @Mock
    private ConceptRestService conceptRestService;

    @Mock
    private ConceptHttpClient conceptHttpClient;

    @Mock
    private TRFeedProperties properties;

    private ConceptFeedWorker conceptFeedWorker;

    private Event event;

    @Before
    public void setup() {
        initMocks(this);
        event = new Event("eventId", "/content", "title", "feedUri");
        properties = createProperties();
        conceptFeedWorker = new ConceptFeedWorker(conceptHttpClient, properties, conceptRestService);
    }

    private TRFeedProperties createProperties() {
        Properties feedDefaults = new Properties();
        feedDefaults.setProperty(TRFeedProperties.TERMINOLOGY_FEED_URI, CONCEPT_BASE_URL);
        return new TRFeedProperties(feedDefaults);
    }

    @Test
    public void shouldSaveOrUpdateTheConceptFetched() throws IOException {
        SimpleObject response = new SimpleObject();

        when(conceptHttpClient.get("http://localhost/content")).thenReturn(response);
        conceptFeedWorker.process(event);
        verify(conceptRestService).save(argumentCaptor.capture());
        assertEquals(response, argumentCaptor.getValue());
    }
}