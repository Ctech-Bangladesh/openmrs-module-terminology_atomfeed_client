package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.mapping.ConceptReferenceTermMapper;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequests;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SHReferenceTermService {

    @Autowired
    private SHConceptSourceService shConceptSourceService;
    @Autowired
    private IdMappingsRepository idMappingsRepository;
    @Autowired
    private ConceptService conceptService;
    @Autowired
    private ConceptReferenceTermMapper conceptReferenceTermMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sync(ConceptReferenceTermRequests conceptReferenceTermRequests) {
        for (ConceptReferenceTermRequest request : conceptReferenceTermRequests.getConceptReferenceTermRequests()) {
            syncReferenceTerm(request);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void syncReferenceTerm(ConceptReferenceTermRequest request) {
        if (request.isHasSource()) {
            shConceptSourceService.sync(request.getConceptSourceRequest());
            IdMapping mapping = idMappingsRepository.findByExternalId(request.getUuid());
            if (null == mapping) {
                ConceptReferenceTerm referenceTerm = conceptReferenceTermMapper.map(request);
                ConceptReferenceTerm savedReferenceTerm = conceptService.saveConceptReferenceTerm(referenceTerm);
                idMappingsRepository.saveMapping(new IdMapping(savedReferenceTerm.getUuid(), request.getUuid()));
            } else {
                ConceptReferenceTerm existingTerm = conceptService.getConceptReferenceTermByUuid(mapping.getInternalId());
                existingTerm.setName(request.getName());
                existingTerm.setCode(request.getCode());
                existingTerm.setDescription(request.getDescription());
                conceptService.saveConceptReferenceTerm(existingTerm);
            }
        }
    }
}
