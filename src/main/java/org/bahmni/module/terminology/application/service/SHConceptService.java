package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mapping.ConceptMapper;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptUpdate;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.application.postprocessor.PostProcessorFactory;
import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ConceptPostProcessor;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.bahmni.module.terminology.application.model.TerminologyClientConstants.CONCEPT;

@Component
public class SHConceptService {

    @Autowired
    private PostProcessorFactory postProcessorFactory;
    @Autowired
    private ConceptService conceptService;
    @Autowired
    private IdMappingsRepository idMappingsRepository;
    @Autowired
    @Qualifier("TRClientConceptMapper")
    private ConceptMapper conceptMapper;
    @Autowired
    private ConceptUpdate conceptUpdate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void sync(ConceptRequest conceptRequest) {
        IdMapping idMapping = idMappingsRepository.findByExternalId(conceptRequest.getUuid());
        Concept newConcept = conceptMapper.map(conceptRequest);
        if (idMapping == null) {
            Concept savedConcept = conceptService.saveConcept(newConcept);
            List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(savedConcept);
            for (ConceptPostProcessor postProcessor : postProcessors) {
                postProcessor.process(savedConcept);
            }
            idMappingsRepository.saveMapping(new IdMapping(savedConcept.getUuid(), conceptRequest.getUuid(), CONCEPT, conceptRequest.getUri()));
        } else {
            Concept existingConcept = conceptService.getConceptByUuid(idMapping.getInternalId());
            conceptUpdate.merge(existingConcept, newConcept);
            conceptService.saveConcept(existingConcept);
            Concept updatedConcept = mergeSpecifics(existingConcept, newConcept);
            List<ConceptPostProcessor> postProcessors = postProcessorFactory.getPostProcessors(updatedConcept);
            for (ConceptPostProcessor postProcessor : postProcessors) {
                postProcessor.process(updatedConcept);
            }
        }
    }

    private Concept mergeSpecifics(Concept existingConcept, Concept newConcept) {
        if (existingConcept instanceof ConceptNumeric && newConcept instanceof ConceptNumeric) {
            return conceptService.saveConcept(conceptUpdate.mergeSpecifics(existingConcept,newConcept));
        }
        return existingConcept;
    }
}
