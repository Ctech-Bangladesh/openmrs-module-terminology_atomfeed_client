package org.bahmni.module.terminology.infrastructure.config;

import org.junit.Test;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class TRFeedPropertiesTest extends BaseModuleWebContextSensitiveTest {

    @Autowired
    private TRFeedProperties feedProperties;


    @Test
    public void shouldIdentifyTerminologyFeedUrl() {
        assertThat(feedProperties.terminologyFeedUri(), is("http://localhost:9997/openmrs/ws/atomfeed/concept/recent"));
    }

    @Test
    public void shouldBuildTheTerminologyResourceUrl() {
        assertThat(feedProperties.getTerminologyUrl("/uuid"), is("http://localhost:9997/uuid"));
    }

    @Test
    public void shouldIdentifyConceptFeedUrl() {
        assertThat(feedProperties.terminologyFeedUri(), is("http://localhost:9997/openmrs/ws/atomfeed/concept/recent"));
    }

    @Test
    public void shouldIdentifyMedicationFeedUrl() {
        assertThat(feedProperties.medicationFeedUri(), is("http://localhost:9997/openmrs/ws/atomfeed/medication/recent"));
    }

    @Test
    public void shouldBuildTheDiagnosisResourceUrl() {
        assertThat(feedProperties.getTerminologyUrl("/diagnosis_id"), is("http://localhost:9997/diagnosis_id"));
    }

    @Test
    public void shouldBuildTheMedicationResourceUrl() {
        assertThat(feedProperties.getTerminologyUrl("/openmrs/ws/rest/v1/tr/drugs/9e240cbc-f133-4d09-9b17-3d0fba236718"), is("http://localhost:9997/openmrs/ws/rest/v1/tr/drugs/9e240cbc-f133-4d09-9b17-3d0fba236718"));
    }

}