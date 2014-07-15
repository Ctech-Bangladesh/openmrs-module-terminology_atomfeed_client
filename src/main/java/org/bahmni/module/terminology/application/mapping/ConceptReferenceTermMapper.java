package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequests;
import org.bahmni.module.terminology.application.model.IdMapping;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class ConceptReferenceTermMapper {

    private ConceptService conceptService;
    private ConceptSourceMapper conceptSourceMapper;
    private IdMappingsRepository idMappingsRepository;

    @Autowired
    public ConceptReferenceTermMapper(ConceptService conceptService, ConceptSourceMapper conceptSourceMapper, IdMappingsRepository idMappingsRepository) {
        this.conceptService = conceptService;
        this.conceptSourceMapper = conceptSourceMapper;
        this.idMappingsRepository = idMappingsRepository;
    }

    public Set<ConceptMap> map(ConceptReferenceTermRequests requests) {
        if (requests.isNotEmpty()) {
            Set<ConceptMap> mappings = new HashSet<>();
            for (ConceptReferenceTermRequest conceptReferenceTermRequest : requests.getConceptReferenceTermRequests()) {
                if (conceptReferenceTermRequest.isHasSource()) {
                    mappings.add(mapConceptReferenceTerm(conceptReferenceTermRequest));
                }
            }
            return mappings;
        } else {
            return null;
        }
    }

    public ConceptReferenceTerm map(ConceptReferenceTermRequest conceptReferenceTermRequest) {
        ConceptReferenceTerm existingReferenceTerm = findExistingConceptReferenceTerm(conceptReferenceTermRequest);
        return (null != existingReferenceTerm) ? existingReferenceTerm : createNew(
                conceptReferenceTermRequest.getCode(),
                conceptReferenceTermRequest.getName(),
                conceptReferenceTermRequest.getDescription(),
                conceptSourceMapper.map(conceptReferenceTermRequest.getConceptSourceRequest())
        );
    }

    private ConceptMap mapConceptReferenceTerm(ConceptReferenceTermRequest conceptReferenceTermRequest) {
        ConceptMap conceptMap = new ConceptMap();
        conceptMap.setConceptMapType(mapConceptMapType(conceptReferenceTermRequest.getMapType()));
        conceptMap.setConceptReferenceTerm(
                map(conceptReferenceTermRequest)
        );
        return conceptMap;
    }

    private ConceptReferenceTerm findExistingConceptReferenceTerm(ConceptReferenceTermRequest request) {
        IdMapping mapping = idMappingsRepository.findByExternalId(request.getUuid());
        if (null != mapping) {
            return conceptService.getConceptReferenceTermByUuid(mapping.getInternalId());
        }
        return null;
    }

    private ConceptMapType mapConceptMapType(String mapType) {
        if (isNotBlank(mapType)) {
            return conceptService.getConceptMapTypeByName(mapType);
        }
        return null;
    }

    private ConceptReferenceTerm createNew(String code, String name, String description, ConceptSource source) {
        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        referenceTerm.setName(name);
        referenceTerm.setCode(code);
        referenceTerm.setDescription(description);
        referenceTerm.setConceptSource(source);
        return referenceTerm;
    }
}
