package org.bahmni.module.terminology.application.service;


import org.bahmni.module.terminology.application.mapping.ConceptSourceMapper;
import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.application.model.TerminologyClientConstants;
import static org.bahmni.module.terminology.application.model.TerminologyClientConstants.CONCEPT_SOURCE;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SHConceptSourceService {

    @Autowired
    private ConceptService conceptService;
    @Autowired
    private ConceptSourceMapper conceptSourceMapper;
    @Autowired
    private IdMappingsRepository idMappingsRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sync(ConceptSourceRequest conceptSourceRequest) {
        if (null != conceptSourceRequest) {
            IdMapping mapping = idMappingsRepository.findByExternalId(conceptSourceRequest.getUuid());
            if (null == mapping) {
                ConceptSource source = conceptSourceMapper.map(conceptSourceRequest);
                ConceptSource conceptSource = conceptService.saveConceptSource(source);
                idMappingsRepository.saveMapping(new IdMapping(conceptSource.getUuid(), conceptSourceRequest.getUuid(), CONCEPT_SOURCE, null));
            } else {
                ConceptSource existingSource = conceptService.getConceptSourceByUuid(mapping.getInternalId());
                existingSource.setName(conceptSourceRequest.getName());
                existingSource.setDescription(conceptSourceRequest.getDescription());
                existingSource.setHl7Code(conceptSourceRequest.getHl7Code());
                conceptService.saveConceptSource(existingSource);
            }
        }
    }
}
