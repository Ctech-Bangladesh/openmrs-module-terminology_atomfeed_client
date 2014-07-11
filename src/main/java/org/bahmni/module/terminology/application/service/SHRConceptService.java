package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mapping.ConceptMapper;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SHRConceptService {

    private ConceptMapper conceptMapper;
    private org.openmrs.api.ConceptService conceptService;
    private SHRConceptSourceService shrConceptSourceService;

    @Autowired
    public SHRConceptService(ConceptMapper conceptMapper, ConceptService conceptService, SHRConceptSourceService shrConceptSourceService) {
        this.conceptMapper = conceptMapper;
        this.conceptService = conceptService;
        this.shrConceptSourceService = shrConceptSourceService;
    }

    public Concept saveConcept(ConceptRequest conceptRequest, ConceptType conceptType) {
        if (conceptService.getConceptByName(conceptRequest.getFullySpecifiedName().getConceptName()) == null) {
            Concept concept = conceptMapper.map(conceptRequest, conceptType);
            shrConceptSourceService.createNonExistentSources(concept);
            return conceptService.saveConcept(concept);
        } else {
            return null;
        }
    }
}
