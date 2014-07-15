package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConceptSourceMapper {

    private ConceptService conceptService;
    private IdMappingsRepository idMappingsRepository;

    @Autowired
    public ConceptSourceMapper(ConceptService conceptService, IdMappingsRepository idMappingsRepository) {
        this.conceptService = conceptService;
        this.idMappingsRepository = idMappingsRepository;
    }

    public ConceptSource map(ConceptSourceRequest conceptSourceRequest) {
        IdMapping idMapping = idMappingsRepository.findByExternalId(conceptSourceRequest.getUuid());
        if (null != idMapping) {
            return conceptService.getConceptSourceByUuid(idMapping.getInternalId());
        } else {
            ConceptSource newConceptSource = new ConceptSource();
            newConceptSource.setName(conceptSourceRequest.getName());
            newConceptSource.setHl7Code(conceptSourceRequest.getHl7Code());
            newConceptSource.setDescription(conceptSourceRequest.getDescription());
            return newConceptSource;
        }
    }
}
