package org.bahmni.module.terminology.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringRegexUtilsTest {

    private StringRegexUtils stringRegexUtils;

    @Before
    public void setUp(){
        stringRegexUtils = new StringRegexUtils();
    }

    @Test
    public void shouldGiveProperBaseUrl(){
        String fullUrl= "http://192.168.33.21:8080/abc/def/er";
        String baseUrl= "http://192.168.33.21:8080";
        assertNotNull(stringRegexUtils.getBaseUrl(fullUrl));
        assertEquals(baseUrl, stringRegexUtils.getBaseUrl(fullUrl));

    }

    @Test
    public void shouldNotValidateBaseUrl(){
        String fullUrl= "http://192.168.33.21:80808080/abc/def/er";
        assertNull(stringRegexUtils.getBaseUrl(fullUrl));
    }

    @Test
    public void shouldValidateOneDigitPort(){
        String fullUrl= "http://192.168.33.21:8/abc/def/er";
        assertNotNull(stringRegexUtils.getBaseUrl(fullUrl));
        assertEquals("http://192.168.33.21:8", stringRegexUtils.getBaseUrl(fullUrl));
    }

    @Test
    public void shouldNotValidateZeroDigitPort(){
        String fullUrl= "http://192.168.33.21:/abc/def/er";
        assertNull(stringRegexUtils.getBaseUrl(fullUrl));
    }

}