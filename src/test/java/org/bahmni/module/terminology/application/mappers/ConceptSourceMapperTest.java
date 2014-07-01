package org.bahmni.module.terminology.application.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConceptSourceMapperTest {

    private final String CONCEPT_SOURCE_NAME = "test-concept-source-name";

    private ArgumentCaptor<ConceptSource> argumentCaptor = ArgumentCaptor.forClass(ConceptSource.class);

    @Mock
    private ConceptService conceptService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void shouldMapAnExistingConceptSource() {
        ConceptSource conceptSource = new ConceptSource();
        when(conceptService.getConceptSourceByName(CONCEPT_SOURCE_NAME)).thenReturn(conceptSource);
        Map<String, Object> data = new HashMap<>();
        data.put("display", CONCEPT_SOURCE_NAME);
        ConceptSourceMapper conceptSourceMapper = new ConceptSourceMapper(conceptService);

        assertThat(conceptSource, is(conceptSourceMapper.map(data)));
    }

    @Test
    public void shouldMapANewConceptSource() {
        Map<String, Object> data = new HashMap<>();
        when(conceptService.getConceptSourceByName(CONCEPT_SOURCE_NAME)).thenReturn(null);
        data.put("display", CONCEPT_SOURCE_NAME);
        ConceptSourceMapper conceptSourceMapper = new ConceptSourceMapper(conceptService);

        conceptSourceMapper.map(data);

        verify(conceptService).saveConceptSource(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(notNullValue()));
        assertThat(argumentCaptor.getValue().getName(), is(CONCEPT_SOURCE_NAME));
    }

}