package org.bahmni.module.terminology.application.mappers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ConceptReferenceTermMapperTest {

    @Mock
    private ConceptService conceptService;
    @Mock
    private ConceptSourceMapper sourceMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldMapAnExistingConceptMap() throws Exception {
        ConceptSource conceptSource = new ConceptSource();
        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        Map<String, Object> mappingData = new HashMap<String, Object>();
        Map<String, Object> sourceData = new HashMap<String, Object>();
        Map<String, Object> refTermData = new HashMap<String, Object>();
        refTermData.put("code", "refcode");
        refTermData.put("name", "refname");
        refTermData.put("description","refdescription");
        refTermData.put("conceptSource", sourceData);
        mappingData.put("conceptReferenceTerm", refTermData);
        when(sourceMapper.map(sourceData)).thenReturn(conceptSource);
        when(conceptService.getConceptReferenceTermByCode("refcode", conceptSource)).thenReturn(referenceTerm);
        ConceptReferenceTermMapper referenceTermMapper = new ConceptReferenceTermMapper(sourceMapper, conceptService);

        ConceptMap conceptMap = referenceTermMapper.map(mappingData);

        assertThat(conceptMap, is(notNullValue()));
    }
}