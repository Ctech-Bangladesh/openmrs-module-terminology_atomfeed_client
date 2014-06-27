package org.bahmni.module.terminology.application.mappers;

import org.junit.Test;
import org.openmrs.ConceptName;
import org.openmrs.api.ConceptNameType;

import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.openmrs.api.ConceptNameType.*;

public class ConceptNameMapperTest {

    @Test
    public void shouldMapToConceptName() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", "testname");
        data.put("locale", "en");
        data.put("conceptNameType", FULLY_SPECIFIED);

        ConceptName conceptName = new ConceptNameMapper().map(data);

        assertThat(conceptName.getName(), is("testname"));
        assertThat(conceptName.getLocale(), is(new Locale("en")));
        assertThat(conceptName.getConceptNameType(), is(FULLY_SPECIFIED));
    }



    @Test
    public void shouldHandleAbsenceOfConceptNameType() throws Exception {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", "testname");
        data.put("locale", "sp");

        ConceptName conceptName = new ConceptNameMapper().map(data);

        assertThat(conceptName.getConceptNameType(), is(nullValue()));

    }
}
