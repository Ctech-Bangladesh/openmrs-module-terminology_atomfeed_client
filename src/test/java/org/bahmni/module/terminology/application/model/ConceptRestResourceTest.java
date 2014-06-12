package org.bahmni.module.terminology.application.model;

import org.bahmni.module.terminology.application.model.ConceptRestResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Enclosed.class)
public class ConceptRestResourceTest {


    public static class WhenSimpleObjectIsValid {

        @Mock
        private static ConceptService conceptService;

        private static SimpleObject object;

        @Before
        public void setup() {
            initMocks(this);
            setupConceptService();
            object = validSimpleObject();
        }

        private void setupConceptService() {
            when(conceptService.getConceptDatatypeByUuid(anyString())).thenReturn(new ConceptDatatype());
            when(conceptService.getConceptClassByUuid(anyString())).thenReturn(new ConceptClass());
        }

        private SimpleObject validSimpleObject() {
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.add("datatype", new HashMap<String, String>());
            simpleObject.add("conceptClass", new HashMap<String, String>());
            return simpleObject;
        }

        @Test
        public void shouldHaveOnlyNonNulValues() throws IOException {
            object.add("key", "value");
            object.add("anotherKey", null);

            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertTrue(conceptRestResource.toDTO(conceptService).keySet().contains("key"));
            assertFalse(conceptRestResource.toDTO(conceptService).keySet().contains("anotherKey"));
        }

        @Test
        public void shouldHaveTheDataTypeId() throws IOException {
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            ((Map) conceptRestResource.toDTO(conceptService).get("datatype")).remove("conceptDatatypeId");
            assertTrue(((Map) conceptRestResource.toDTO(conceptService).get("datatype")).keySet().contains("conceptDatatypeId"));
        }

        @Test
        public void shouldHaveTheConceptClassId() throws IOException {
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            ((Map) conceptRestResource.toDTO(conceptService).get("datatype")).remove("conceptClassId");
            assertTrue(((Map) conceptRestResource.toDTO(conceptService).get("conceptClass")).keySet().contains("conceptClassId"));
        }

        @Test
        public void shouldNotHaveUUID() throws IOException {
            object.add("uuid", "someRandomUUID");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO(conceptService).get("uuid"));
        }

        @Test
        public void shouldNotHavePreciseSetting() throws IOException {
            object.add("precise", "true");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO(conceptService).get("precise"));
        }

        @Test
        public void shouldNotHaveAuditInfo() throws IOException {
            object.add("auditInfo", "auditInfo");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO(conceptService).get("auditInfo"));
        }
    }

    public static class WhenConceptResourceDoesNotHaveDataType {

        @Mock
        private static ConceptService conceptService;

        private static SimpleObject object;

        @Before
        public void setup() {
            initMocks(this);
            setupConceptService();
            object = validSimpleObject();
        }

        private void setupConceptService() {
            when(conceptService.getConceptDatatypeByUuid(anyString())).thenThrow(new RuntimeException("Datatype id should not be null"));
            when(conceptService.getConceptClassByUuid(anyString())).thenReturn(new ConceptClass());
        }

        private SimpleObject validSimpleObject() {
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.add("conceptClass", new HashMap<String, String>());
            return simpleObject;
        }

        @Test(expected = IllegalArgumentException.class)
        public void shouldThrowExceptionOnInit() throws IOException {
            new ConceptRestResource(object);
        }
    }

    public static class WhenConceptResourceDoesNotHaveConceptClass {

        @Mock
        private static ConceptService conceptService;

        private static SimpleObject object;

        @Before
        public void setup() {
            initMocks(this);
            setupConceptService();
            object = validSimpleObject();
        }

        private void setupConceptService() {
            when(conceptService.getConceptDatatypeByUuid(anyString())).thenReturn(new ConceptDatatype());
            when(conceptService.getConceptClassByUuid(anyString())).thenThrow(new RuntimeException("concept class id should not be null"));
        }

        private SimpleObject validSimpleObject() {
            SimpleObject simpleObject = new SimpleObject();
            simpleObject.add("datatype", new HashMap<String, String>());
            return simpleObject;
        }

        @Test(expected = IllegalArgumentException.class)
        public void shouldThrowExceptionOnInit() throws IOException {
            new ConceptRestResource(object);
        }
    }
}