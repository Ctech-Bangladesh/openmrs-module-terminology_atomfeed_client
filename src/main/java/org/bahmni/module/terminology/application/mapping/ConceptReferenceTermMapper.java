package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequests;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class ConceptReferenceTermMapper {

    private ConceptService conceptService;
    private ConceptSourceMapper conceptSourceMapper;

    @Autowired
    public ConceptReferenceTermMapper(ConceptService conceptService, ConceptSourceMapper conceptSourceMapper) {
        this.conceptService = conceptService;
        this.conceptSourceMapper = conceptSourceMapper;
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

    private ConceptMap mapConceptReferenceTerm(ConceptReferenceTermRequest conceptReferenceTermRequest) {
        ConceptMap conceptMap = new ConceptMap();
        conceptMap.setConceptMapType(mapConceptMapType(conceptReferenceTermRequest.getMapType()));
        conceptMap.setConceptReferenceTerm(
                createNew(
                        conceptReferenceTermRequest.getCode(),
                        conceptReferenceTermRequest.getName(),
                        conceptReferenceTermRequest.getDescription(),
                        conceptSourceMapper.map(conceptReferenceTermRequest.getConceptSourceRequest())
                )
        );
        return conceptMap;
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
