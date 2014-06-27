package org.bahmni.module.terminology.application.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConceptMapperTest {

    @Mock
    private ConceptService conceptService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldMapDataToConcept() throws Exception {
        when(conceptService.getConceptDatatypeByName("test-data-type")).thenReturn(new ConceptDatatype());
        when(conceptService.getConceptClassByName("test-concept-class")).thenReturn(new ConceptClass());
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> coneptName = new HashMap<String, Object>();
        coneptName.put("coneptName", "test-coneptName");
        coneptName.put("locale", "en");
        data.put("name", coneptName);
        data.put("names", Arrays.asList(coneptName));
        data.put("resourceVersion", "1");
        data.put("datatype", dataType());
        data.put("conceptClass", conceptClass());

        Concept concept = new ConceptMapper(new ConceptNameMapper()).map(data, conceptService);

        assertThat(concept.getNames().size(), is(1));
        assertThat(concept.getConceptClass(), is(notNullValue()));
        assertThat(concept.getDatatype(), is(notNullValue()));

    }

    private HashMap<String, Object> conceptClass() {
        HashMap<String, Object> conceptClass = new HashMap<String, Object>();
        conceptClass.put("name", "test-concept-class");
        return conceptClass;
    }

    private HashMap<String, Object> dataType() {
        HashMap<String, Object> dataType = new HashMap<String, Object>();
        dataType.put("name", "test-data-type");
        return dataType;
    }
}