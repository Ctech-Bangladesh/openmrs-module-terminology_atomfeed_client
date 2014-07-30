package org.bahmni.module.terminology.application.mapping;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptDescriptionRequest;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.openmrs.Concept;
import org.openmrs.ConceptDescription;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ConceptMapper {

    private ConceptService conceptService;
    private ConceptNameMapper conceptNameMapper;
    private ConceptReferenceTermMapper conceptReferenceTermMapper;

    @Autowired
    public ConceptMapper(ConceptService conceptService, ConceptNameMapper conceptNameMapper, ConceptReferenceTermMapper conceptReferenceTermMapper) {
        this.conceptService = conceptService;
        this.conceptNameMapper = conceptNameMapper;
        this.conceptReferenceTermMapper = conceptReferenceTermMapper;
    }

    public Concept map(ConceptRequest conceptRequest) {
        Concept concept = new Concept();
        concept.setFullySpecifiedName(conceptNameMapper.map(conceptRequest.getFullySpecifiedName()));
        concept.setSet(conceptRequest.isSet());
        concept.setRetired(conceptRequest.isRetired());
        concept.setRetireReason(conceptRequest.getRetireReason());
        concept.setVersion(conceptRequest.getVersion());
        mapConceptDatatype(concept, conceptRequest.getDatatypeName());
        mapConceptClass(concept, conceptRequest);
        concept.addDescription(mapDescriptions(conceptRequest.getConceptDescriptionRequest()));
        concept.setNames(conceptNameMapper.map(conceptRequest.getConceptNameRequests()));
        concept.setConceptMappings(conceptReferenceTermMapper.map(conceptRequest.getConceptReferenceTermRequests()));
        return concept;
    }

    private ConceptDescription mapDescriptions(ConceptDescriptionRequest conceptDescriptionRequest) {
        if (null == conceptDescriptionRequest) {
            return null;
        }
        ConceptDescription description = new ConceptDescription();
        description.setDescription(conceptDescriptionRequest.getDescription());
        description.setLocale(Locale.ENGLISH);
        return description;
    }

    private void mapConceptClass(Concept concept, ConceptRequest conceptRequest) {
        concept.setConceptClass(conceptService.getConceptClassByName(conceptRequest.getConceptClass()));
    }

    private void mapConceptDatatype(Concept concept, String datatypeName) {
        if (StringUtils.isNotBlank(datatypeName)) {
            concept.setDatatype(conceptService.getConceptDatatypeByName(datatypeName));
        }
    }
}
