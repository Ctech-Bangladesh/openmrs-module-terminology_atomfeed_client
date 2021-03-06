package org.bahmni.module.terminology.infrastructure.mapper;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.CollectionUtil.safeGetMap;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptReferenceTermRequestMapper {

    private ConceptSourceRequestMapper conceptSourceRequestMapper;
    private ConceptReferenceTermMapRequestMapper conceptReferenceTermMapRequestMapper;

    @Autowired
    public ConceptReferenceTermRequestMapper(ConceptSourceRequestMapper conceptSourceRequestMapper, ConceptReferenceTermMapRequestMapper conceptReferenceTermMapRequestMapper) {
        this.conceptSourceRequestMapper = conceptSourceRequestMapper;
        this.conceptReferenceTermMapRequestMapper = conceptReferenceTermMapRequestMapper;
    }

    public ConceptReferenceTermRequests mapFromConceptRequest(Object data) {
        ConceptReferenceTermRequests result = new ConceptReferenceTermRequests();
        Collection conceptReferenceTerms = (Collection) data;
        if (null != conceptReferenceTerms) {
            for (Object conceptReferenceTerm : conceptReferenceTerms) {
                ConceptReferenceTermRequest referenceTermRequest = mapReferenceTerm((Map) conceptReferenceTerm);
                referenceTermRequest.setMapType(asString(safeGet((Map) conceptReferenceTerm, "mapType", StringUtils.EMPTY)));
                result.add(referenceTermRequest);
            }
        }
        return result;
    }

    public ConceptReferenceTermRequest mapFromConceptReferenceTermRequest(Map conceptReferenceTermRequestMap) {
        ConceptReferenceTermRequest conceptReferenceTermRequest = mapReferenceTerm(conceptReferenceTermRequestMap);
        if (null != conceptReferenceTermRequestMap.get("conceptReferenceTermMaps")) {
            conceptReferenceTermRequest.setConceptReferenceTermMapRequests(conceptReferenceTermMapRequestMapper.map((Collection<Object>) safeGet(conceptReferenceTermRequestMap, "conceptReferenceTermMaps")));
        }
        return conceptReferenceTermRequest;
    }

    public ConceptReferenceTermRequest mapReferenceTerm(Map conceptReferenceTermMap) {
        ConceptReferenceTermRequest conceptReferenceTermRequest = new ConceptReferenceTermRequest();
        conceptReferenceTermRequest.setCode(asString(safeGet(conceptReferenceTermMap, "code")));
        conceptReferenceTermRequest.setRetired(Boolean.valueOf(asString(safeGet(conceptReferenceTermMap, "retired"))));
        conceptReferenceTermRequest.setVersion(asString(safeGet(conceptReferenceTermMap, "version")));
        conceptReferenceTermRequest.setUuid(asString(safeGet(conceptReferenceTermMap, "uuid")));
        conceptReferenceTermRequest.setUri(asString(safeGet(conceptReferenceTermMap, "uri")));
        conceptReferenceTermRequest.setName(asString(safeGet(conceptReferenceTermMap, "name")));
        conceptReferenceTermRequest.setDescription(asString(safeGet(conceptReferenceTermMap, "description")));
        conceptReferenceTermRequest.setConceptSourceRequest(conceptSourceRequestMapper.map(safeGetMap(conceptReferenceTermMap, "conceptSource")));
        return conceptReferenceTermRequest;
    }
}
