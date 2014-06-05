package org.bahmni.module.terminology;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TRFeedPropertiesTest {

    private Properties properties;

    @Before
    public void setUp(){
        properties = new Properties();
        properties.setProperty("terminology.feed.url", "http://127.0.0.1:9080/openmrs/ws/atomfeed/concept/recent");
    }

    @Test
    public void shouldIdentifyTerminologyFeedUrl(){
        TRFeedProperties trFeedProperties = new TRFeedProperties(properties);

        assertThat(trFeedProperties.terminologyFeedUri(), is("http://127.0.0.1:9080/openmrs/ws/atomfeed/concept/recent"));
    }

    @Test
    public void shouldIdentifyTerminologyServerPrefix(){
        TRFeedProperties trFeedProperties = new TRFeedProperties(properties);

        assertThat(trFeedProperties.terminologyServerPrefix(), is("http://127.0.0.1:9080"));
    }
}