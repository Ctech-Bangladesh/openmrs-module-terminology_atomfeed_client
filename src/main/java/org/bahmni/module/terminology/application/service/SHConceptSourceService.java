package org.bahmni.module.terminology.application.service;


import org.apache.commons.lang3.StringUtils;
import org.bahmni.module.terminology.application.mapping.ConceptSourceMapper;
import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.bahmni.module.terminology.application.model.TerminologyClientConstants.CONCEPT_SOURCE;

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
            ConceptSource mappedSource = conceptSourceMapper.map(conceptSourceRequest);
            ConceptSource existingSource = null;
            if ((mappedSource.getId() == null) || (mappedSource.getId() < 0)) {
                List<ConceptSource> allConceptSources = conceptService.getAllConceptSources(true);
                for (ConceptSource source : allConceptSources) {
                    String hl7Code = source.getHl7Code();
                    if (StringUtils.isNotBlank(hl7Code) && hl7Code.equalsIgnoreCase(conceptSourceRequest.getHl7Code())) {
                        existingSource = source;
                    }
                }

                if (existingSource == null) {
                    ConceptSource conceptSource = conceptService.saveConceptSource(mappedSource);
                    idMappingsRepository.saveMapping(new IdMapping(conceptSource.getUuid(), conceptSourceRequest.getUuid(), CONCEPT_SOURCE, null));
                    return;
                } else {
                    //create a mapping with existing source
                    idMappingsRepository.saveMapping(new IdMapping(existingSource.getUuid(), conceptSourceRequest.getUuid(), CONCEPT_SOURCE, null));
                }
            } else {
                existingSource = mappedSource;
            }

            //check if existing source needs to be updated
            if (needsUpdate(existingSource, conceptSourceRequest)) {
                existingSource.setName(conceptSourceRequest.getName());
                existingSource.setDescription(conceptSourceRequest.getDescription());
                conceptService.saveConceptSource(existingSource);
            }
        }
    }

    private boolean needsUpdate(ConceptSource existingSource, ConceptSourceRequest conceptSourceRequest) {
        if (!existingSource.getName().equalsIgnoreCase(conceptSourceRequest.getName())) {
            return true;
        }
        //Should we compare description too?
        return false;
    }
}
