package org.bahmni.module.terminology.application.dtos;

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Enclosed.class)
public class ConceptRestResourceTest {

    private static final Integer DATA_TYPE_ID = 1;
    private static final Integer CLASS_ID = 2;

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
            simpleObject.add("datatype", DATA_TYPE_ID);
            simpleObject.add("conceptClass", CLASS_ID);
            return simpleObject;
        }

        @Test
        public void shouldHaveOnlyNonNulValues() throws IOException {
            object.add("key", "value");
            object.add("anotherKey", null);

            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertTrue(conceptRestResource.toDTO().keySet().contains("key"));
            assertFalse(conceptRestResource.toDTO().keySet().contains("anotherKey"));
        }

        @Test
        public void shouldHaveTheDataTypeId() throws IOException {
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertTrue(conceptRestResource.toDTO().get("datatype").equals(DATA_TYPE_ID));
        }

        @Test
        public void shouldHaveTheConceptClassId() throws IOException {
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertTrue(conceptRestResource.toDTO().get("conceptClass").equals(CLASS_ID));
        }

        @Test
        public void shouldNotHaveUUID() throws IOException {
            object.add("uuid", "someRandomUUID");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO().get("uuid"));
        }

        @Test
        public void shouldNotHavePreciseSetting() throws IOException {
            object.add("precise", "true");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO().get("precise"));
        }

        @Test
        public void shouldNotHaveAuditInfo() throws IOException {
            object.add("auditInfo", "auditInfo");
            ConceptRestResource conceptRestResource = new ConceptRestResource(object);
            assertNull(conceptRestResource.toDTO().get("auditInfo"));
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