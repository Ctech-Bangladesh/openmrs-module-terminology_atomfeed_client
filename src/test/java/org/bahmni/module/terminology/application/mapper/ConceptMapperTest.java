package org.bahmni.module.terminology.application.mapper;

import org.bahmni.module.terminology.application.model.ConceptObject;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConceptMapperTest {

    private static final String CONCEPT_NAME = "test_concept_class";
    private static final String TEST_CONCEPT_NAME = "test_concept_name";
    private static final String CONCEPT_CLASS_UUID = "concept_uuid";
    private final String DATA_TYPE_NAME = "DATA_TYPE_NAME";

    @Mock
    private ConceptService conceptService;
    private ConceptMapper mapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(conceptService.getConceptDatatypeByName(DATA_TYPE_NAME)).thenReturn(new ConceptDatatype());
        when(conceptService.getConceptClassByName(CONCEPT_NAME)).thenReturn(new ConceptClass());

        mapper = new ConceptMapper(new ConceptNameMapper(), conceptService);
    }

    @Test
    public void shouldMapSimpleObjectToConceptObject() {

        ConceptObject conceptObject = mapper.map(simpleObject());

        assertThat(conceptObject, is(notNullValue()));
        assertThat(conceptObject.getDisplay(), is("test-display"));
        assertThat(conceptObject.getUuid(), is("test-uuid"));
        assertThat(conceptObject.getRetired(), is("false"));
        assertThat(conceptObject.getVersion(), is("1"));
        assertThat(conceptObject.getConceptName().getDisplay(), is(TEST_CONCEPT_NAME));
        assertThat(conceptObject.getConceptClass().getUuid(), is(CoreMatchers.notNullValue()));

    }

    private SimpleObject simpleObject() {
        SimpleObject simpleObject = new SimpleObject();
        simpleObject.put("uuid", "test-uuid");
        simpleObject.put("display", "test-display");
        simpleObject.put("name", conceptName());
        simpleObject.put("names", names());
        simpleObject.put("retired", "false");
        simpleObject.put("set", "set");
        simpleObject.put("resourceVersion", "1");
        simpleObject.put("datatype", dataType());
        simpleObject.put("conceptClass", conceptClass());
        return simpleObject;
    }

    private List<Map<String, String>> names(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", TEST_CONCEPT_NAME);
        return asList(map);
    }

    private HashMap<String, String> conceptName() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("display", TEST_CONCEPT_NAME);
        return map;
    }

    private HashMap<String, String> conceptClass() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("display", CONCEPT_NAME);
        map.put("uuid", CONCEPT_CLASS_UUID);
        map.put("conceptClass", CONCEPT_NAME);
        return map;
    }

    private HashMap<String, String> dataType() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("display", DATA_TYPE_NAME);
        return map;
    }
}