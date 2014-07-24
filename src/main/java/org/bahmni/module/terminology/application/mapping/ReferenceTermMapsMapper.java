package org.bahmni.module.terminology.application.mapping;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermMapRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermMapRequests;
import org.bahmni.module.terminology.infrastructure.repository.IdMappingsRepository;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptReferenceTermMap;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReferenceTermMapsMapper {

    private ConceptService conceptService;
    private IdMappingsRepository idMappingsRepository;

    @Autowired
    public ReferenceTermMapsMapper(ConceptService conceptService, IdMappingsRepository idMappingsRepository) {
        this.conceptService = conceptService;
        this.idMappingsRepository = idMappingsRepository;
    }

    public ConceptReferenceTerm map(ConceptReferenceTerm referenceTerm, ConceptReferenceTermMapRequests conceptReferenceTermMapRequests) {
        if (null == conceptReferenceTermMapRequests) {
            return referenceTerm;
        } else {
            Set<ConceptReferenceTermMap> conceptReferenceTermMaps = referenceTerm.getConceptReferenceTermMaps();
            for (ConceptReferenceTermMapRequest request : conceptReferenceTermMapRequests.selectNewMappings(conceptReferenceTermMaps)) {
                referenceTerm.addConceptReferenceTermMap(mapReferenceTermMap(referenceTerm, request, new ConceptReferenceTermMap()));
            }
            for (ConceptReferenceTermMapRequest request : conceptReferenceTermMapRequests.selectExistingMappings(conceptReferenceTermMaps)) {
                mapReferenceTermMap(referenceTerm, request, find(conceptReferenceTermMaps, request));
            }
            return referenceTerm;
        }
    }

    private ConceptReferenceTermMap find(Set<ConceptReferenceTermMap> conceptReferenceTermMaps, ConceptReferenceTermMapRequest request) {
        for (ConceptReferenceTermMap conceptReferenceTermMap : conceptReferenceTermMaps) {
            if (StringUtils.equals(conceptReferenceTermMap.getUuid(), request.getUuid())) {
                return conceptReferenceTermMap;
            }
        }
        return null;
    }

    private ConceptReferenceTermMap mapReferenceTermMap(ConceptReferenceTerm referenceTerm, ConceptReferenceTermMapRequest request, ConceptReferenceTermMap map) {
        map.setUuid(request.getUuid());
        map.setConceptMapType(conceptService.getConceptMapTypeByName("SAME-AS"));
        map.setTermA(referenceTerm);
        map.setTermB(conceptService.getConceptReferenceTermByUuid(idMappingsRepository.findByExternalId(request.getTermB().getUuid()).getInternalId()));
        return map;
    }

}
