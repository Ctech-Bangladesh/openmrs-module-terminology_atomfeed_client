package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mapping.ConceptMapper;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.application.model.PersistedConcept;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.DiagnosisPostProcessor;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SHRConceptService {

    private ConceptMapper conceptMapper;
    private ConceptService conceptService;
    private SHRConceptSourceService shrConceptSourceService;
    private DiagnosisPostProcessor diagnosisPostProcessor;
    private SHRConceptReferenceTermService shrConceptReferenceTermService;
    private IdMappingsRepository idMappingsRepository;

    @Autowired
    public SHRConceptService(ConceptMapper conceptMapper,
                             ConceptService conceptService,
                             SHRConceptSourceService shrConceptSourceService,
                             DiagnosisPostProcessor diagnosisPostProcessor,
                             SHRConceptReferenceTermService shrConceptReferenceTermService,
                             IdMappingsRepository idMappingsRepository) {
        this.conceptMapper = conceptMapper;
        this.conceptService = conceptService;
        this.shrConceptSourceService = shrConceptSourceService;
        this.diagnosisPostProcessor = diagnosisPostProcessor;
        this.shrConceptReferenceTermService = shrConceptReferenceTermService;
        this.idMappingsRepository = idMappingsRepository;
    }

    public void saveConcept(ConceptRequest conceptRequest, ConceptType conceptType) {
        Concept existingConcept = conceptService.getConceptByName(conceptRequest.getFullySpecifiedName().getConceptName());
        Concept newConcept = conceptMapper.map(conceptRequest, conceptType);
        if (existingConcept == null) {
            shrConceptSourceService.createNonExistentSources(newConcept);
            shrConceptReferenceTermService.mergeExistingConceptReferenceTerms(newConcept);
            Concept savedConcept = conceptService.saveConcept(newConcept);
            diagnosisPostProcessor.process(savedConcept);
            idMappingsRepository.saveMapping(new IdMapping(savedConcept.getUuid(), conceptRequest.getUuid()));
        } else {
            Concept updatedConcept = new PersistedConcept(existingConcept).merge(newConcept);
            conceptService.saveConcept(updatedConcept);
        }
    }
}
