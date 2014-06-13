package org.openmrs.module.webservices.rest;

import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class SimpleObjectTest {

    @Test
    public void shouldParseJsonString() throws IOException {
        SimpleObject simpleObject = SimpleObject.parseJson("{\"key\" : \"value\"}");
        assertEquals("value", simpleObject.get("key").toString());
    }

    @Test(expected = ClassCastException.class)
    public void shouldNotWrapNestedMapsWithinSimpleObject() throws IOException {
        SimpleObject simpleObject = SimpleObject.parseJson("{\"key\" : {\"innerKey\": \"innerValue\"}}");
        assertEquals("innerValue", ((SimpleObject) simpleObject.get("key")).get("innerKey").toString());
    }

    @Test
    public void shouldReturnLinkedHashMapForNestedMap() throws IOException {
        SimpleObject simpleObject = SimpleObject.parseJson("{\"key\" : {\"innerKey\": \"innerValue\"}}");
        assertEquals("innerValue", ((LinkedHashMap) simpleObject.get("key")).get("innerKey").toString());
    }
}
