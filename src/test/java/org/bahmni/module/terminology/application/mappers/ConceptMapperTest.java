package org.bahmni.module.terminology.application.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.bahmni.module.terminology.util.CollectionUtils.first;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConceptMapperTest {

    private final String CONCEPT_SOURCE_CODE = "ICD10-BD";
    @Mock
    private ConceptService conceptService;
    private String DATA_TYPE_NAME = "test-data-type";
    private String CLASS_NAME = "test-concept-class";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldMapDataToConcept() throws Exception {
        when(conceptService.getConceptDatatypeByName(DATA_TYPE_NAME)).thenReturn(new ConceptDatatype());
        when(conceptService.getConceptClassByName(CLASS_NAME)).thenReturn(new ConceptClass());
        Map<String, Object> data = basicConceptData();

        Concept concept = new ConceptMapper(new ConceptNameMapper(), new ReferenceTermMapper()).map(data, conceptService);

        assertThat(concept.getNames().size(), is(1));
        assertThat(concept.getConceptClass(), is(notNullValue()));
        assertThat(concept.getDatatype(), is(notNullValue()));
    }

    @Test
    public void shouldMapDataWithRefTermToConcept() {
        ConceptSource conceptSource = new ConceptSource();
        conceptSource.setName(CONCEPT_SOURCE_CODE);
        when(conceptService.getConceptSourceByName(CONCEPT_SOURCE_CODE)).thenReturn(conceptSource);
        Map<String, Object> data = basicConceptData();
        data.put("mappings", asList(mapping()));

        Concept concept = new ConceptMapper(new ConceptNameMapper(), new ReferenceTermMapper()).map(data, conceptService);

        ConceptMap mappings = first(concept.getConceptMappings());
        assertThat(mappings.getConceptReferenceTerm().getCode(), is("A00"));
        assertThat(mappings.getConceptReferenceTerm().getName(), is("Cholera"));
        assertThat(mappings.getConceptReferenceTerm().getConceptSource().getName(), is("ICD10-BD"));

    }

    private Map<String, Object> mapping() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("conceptReferenceTerm", referenceTerm());
        return data;
    }

    private Map<String, Object> referenceTerm() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("conceptSource", conceptSource());
        data.put("code", "A00");
        data.put("name", "Cholera");
        return data;
    }

    private Object conceptSource() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("display", CONCEPT_SOURCE_CODE);
        return data;
    }

    private Map<String, Object> basicConceptData() {
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> coneptName = new HashMap<String, Object>();
        coneptName.put("coneptName", "test-coneptName");
        coneptName.put("locale", "en");
        data.put("name", coneptName);
        data.put("names", asList(coneptName));
        data.put("resourceVersion", "1");
        data.put("datatype", dataType());
        data.put("conceptClass", conceptClass());
        return data;
    }

    private HashMap<String, Object> conceptClass() {
        HashMap<String, Object> conceptClass = new HashMap<String, Object>();
        conceptClass.put("name", CLASS_NAME);
        return conceptClass;
    }

    private HashMap<String, Object> dataType() {
        HashMap<String, Object> dataType = new HashMap<String, Object>();
        dataType.put("name", DATA_TYPE_NAME);
        return dataType;
    }
}