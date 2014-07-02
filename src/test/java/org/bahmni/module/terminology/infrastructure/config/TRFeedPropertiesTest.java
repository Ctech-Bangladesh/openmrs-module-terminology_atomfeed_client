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
        assertThat(feedProperties.terminologyFeedUri(), is("http://localhost:9999/openmrs/ws/atomfeed/concept/recent"));
    }

    @Test
    public void shouldBuildTheTerminologyResourceUrl() {
        assertThat(feedProperties.getTerminologyUrl("/uuid"), is("http://localhost:9999/uuid"));
    }

    @Test
    public void shouldIdentifyDiagnosisFeedUrl() {
        assertThat(feedProperties.getDiagnosisFeedUrl(), is("http://localhost:9999/openmrs/ws/atomfeed/diagnosis/recent"));
    }

    @Test
    public void shouldBuildTheDiagnosisResourceUrl() {
        assertThat(feedProperties.getDiagnosisUrl("/diagnosis_id"), is("http://localhost:9999/diagnosis_id"));
    }

}