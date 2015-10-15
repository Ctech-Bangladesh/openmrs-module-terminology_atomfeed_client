package org.bahmni.module.terminology.infrastructure.http;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.codec.binary.Base64;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthenticatedHttpClientTest {
    public static final String TR_CONCEPT_URL = "http://localhost:8089/openmrs/ws/rest/v1/tr/concepts/56b19321-7311-11e5-8560-08002715d519";
    @Mock
    TRFeedProperties properties;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    private Map<String, String> getBasicAuthHeader(String user, String password) {
        HashMap<String, String> header = new HashMap<>();
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
        header.put("Authorization", "Basic " + new String(encodedAuth));
        return header;
    }

    @Test
    public void shouldFailForUnauthorizedUser() throws Exception {
        when(properties.getTerminologyApiUserName()).thenReturn("user");
        when(properties.getTerminologyApiUserPassword()).thenReturn("password");
        stubFor(get(urlMatching("/openmrs/ws/rest/v1/tr/concepts/56b19321-7311-11e5-8560-08002715d519")).atPriority(5).willReturn(aResponse().withStatus(401)));
        AuthenticatedHttpClient client = new AuthenticatedHttpClient(properties);
        try {
            client.get(TR_CONCEPT_URL, HashMap.class);
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), AuthenticatedHttpClient.USER_NOT_AUTHORIZED);
            return;
        }
        fail("Should have thrown User not authorized error");
    }
}