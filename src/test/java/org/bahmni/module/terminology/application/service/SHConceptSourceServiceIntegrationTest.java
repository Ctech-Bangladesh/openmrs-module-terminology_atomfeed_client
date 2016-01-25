package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.junit.Test;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@org.springframework.test.context.ContextConfiguration(locations = {"classpath:TestingApplicationContext.xml"}, inheritLocations = true)
public class SHConceptSourceServiceIntegrationTest extends BaseModuleWebContextSensitiveTest {

    @Autowired
    private SHConceptSourceService conceptSourceService;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private IdMappingsRepository idMappingsRepository;

    @Test
    public void shouldSaveANewConceptReferenceSource() throws Exception {
        String name = "name";
        String uuid = "uuid";
        String hl7code = "hl7code";
        String description = "description";
        assertNull(conceptService.getConceptSourceByName(name));

        ConceptSourceRequest conceptSourceRequest = createConceptSourceRequest(name, uuid, hl7code, description);

        conceptSourceService.sync(conceptSourceRequest);

        ConceptSource savedConceptSource = conceptService.getConceptSourceByName(name);
        assertNotNull(savedConceptSource);
        IdMapping mapping = idMappingsRepository.findByExternalId(uuid);
        assertNotNull(mapping);
        assertEquals(savedConceptSource.getUuid(), mapping.getInternalId());
        assertConceptSource(savedConceptSource, name, hl7code, description);
    }

    @Test
    public void shouldNotCreateANewConceptSourceWhenAlreadyExistsWithSameHL7Code() throws Exception {
        executeDataSet("stubdata/datasets/shConceptServiceTestDs.xml");
        String name = "WHO";
        String uuid = "uuid";
        String hl7code = "WHO";
        String description = "description";
        ConceptSourceRequest conceptSourceRequest = createConceptSourceRequest(name, uuid, hl7code, description);

        ConceptSource whoConceptSource = conceptService.getConceptSource(11);

        conceptSourceService.sync(conceptSourceRequest);

        ConceptSource conceptSource = conceptService.getConceptSourceByName(name);
        assertNotNull(conceptSource);
        IdMapping mapping = idMappingsRepository.findByExternalId(uuid);
        assertNotNull(mapping);
        assertEquals(whoConceptSource.getUuid(), mapping.getInternalId());
        assertEquals(conceptSource, whoConceptSource);
    }

    @Test
    public void shouldUpdateTheExistingConceptSourceHasADifferentName() throws Exception {
        executeDataSet("stubdata/datasets/shConceptServiceTestDs.xml");
        String nameUpdated = "WHO-Updated";
        String uuid = "uuid-updated";
        String hl7code = "WHO";
        String descriptionUpdated = "description-updated";
        ConceptSourceRequest conceptSourceRequest = createConceptSourceRequest(nameUpdated, uuid, hl7code, descriptionUpdated);

        ConceptSource whoConceptSource = conceptService.getConceptSource(11);
        assertConceptSource(whoConceptSource, "WHO", hl7code, "A source for WHO");

        conceptSourceService.sync(conceptSourceRequest);

        ConceptSource conceptSource = conceptService.getConceptSourceByName(nameUpdated);
        assertNotNull(conceptSource);
        IdMapping mapping = idMappingsRepository.findByExternalId(uuid);
        assertNotNull(mapping);
        assertEquals(whoConceptSource.getUuid(), mapping.getInternalId());
        assertEquals(conceptSource, whoConceptSource);
        assertConceptSource(whoConceptSource, nameUpdated, hl7code, descriptionUpdated);
    }

    @Test
    public void shouldUpdateTheConceptSourceIfThereIsAMappingExistForIt() throws Exception {
        executeDataSet("stubdata/datasets/shConceptServiceTestDs.xml");
        String nameUpdated = "ICD-Updated";
        String uuid = "source123";
        String hl7code = "I10";
        String descriptionUpdated = "description-updated";
        ConceptSourceRequest conceptSourceRequest = createConceptSourceRequest(nameUpdated, uuid, hl7code, descriptionUpdated);

        ConceptSource existingConceptSource = conceptService.getConceptSource(12);
        IdMapping existingMapping = idMappingsRepository.findByInternalId(existingConceptSource.getUuid());
        assertConceptSource(existingConceptSource, "ICD", hl7code, "A source for ICD");

        conceptSourceService.sync(conceptSourceRequest);

        ConceptSource conceptSource = conceptService.getConceptSourceByName(nameUpdated);
        IdMapping mapping = idMappingsRepository.findByExternalId(uuid);
        assertNotNull(mapping);
        assertNotNull(conceptSource);
        assertEquals(mapping.getId(), existingMapping.getId());
        assertEquals(conceptSource, existingConceptSource);
        assertConceptSource(existingConceptSource, nameUpdated, hl7code, descriptionUpdated);

    }

    private void assertConceptSource(ConceptSource savedConceptSource, String name, String hl7code, String description) {
        assertEquals(name, savedConceptSource.getName());
        assertEquals(hl7code, savedConceptSource.getHl7Code());
        assertEquals(description, savedConceptSource.getDescription());
    }

    private ConceptSourceRequest createConceptSourceRequest(String name, String uuid, String hl7code, String description) {
        ConceptSourceRequest conceptSourceRequest = new ConceptSourceRequest();
        conceptSourceRequest.setName(name);
        conceptSourceRequest.setDescription(description);
        conceptSourceRequest.setHl7Code(hl7code);
        conceptSourceRequest.setUuid(uuid);
        return conceptSourceRequest;
    }
}