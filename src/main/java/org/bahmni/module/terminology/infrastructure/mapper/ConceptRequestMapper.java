package org.bahmni.module.terminology.infrastructure.mapper;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.CollectionUtil.safeGetMap;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptRequestMapper {

    private ConceptNameRequestMapper conceptNameRequestMapper;
    private ConceptReferenceTermRequestMapper conceptReferenceTermRequestMapper;
    private ConceptDescriptionRequestMapper conceptDescriptionRequestMapper;

    @Autowired
    public ConceptRequestMapper(ConceptNameRequestMapper conceptNameRequestMapper, ConceptReferenceTermRequestMapper conceptReferenceTermRequestMapper, ConceptDescriptionRequestMapper conceptDescriptionRequestMapper) {
        this.conceptNameRequestMapper = conceptNameRequestMapper;
        this.conceptReferenceTermRequestMapper = conceptReferenceTermRequestMapper;
        this.conceptDescriptionRequestMapper = conceptDescriptionRequestMapper;
    }

    public ConceptRequest map(Map<String, Object> data) {
        ConceptRequest request = new ConceptRequest();
        request.setFullySpecifiedName(conceptNameRequestMapper.mapNameRequest(safeGetMap(data, "name")));
        request.setConceptClass(asString(safeGet(safeGetMap(data, "conceptClass"), "name")));
        request.setDatatypeName(asString(safeGet(safeGetMap(data, "datatype"), "name")));
        request.setSet(Boolean.valueOf(asString(safeGet(data, "set", "false"))));
        request.setVersion(asString(safeGet(data, "version", StringUtils.EMPTY)));
        request.setConceptNameRequests(conceptNameRequestMapper.map(data.get("names")));
        request.setConceptReferenceTermRequests(conceptReferenceTermRequestMapper.map(data.get("mappings")));
        request.setUuid(asString(safeGet(data, "uuid")));
        request.setConceptDescriptionRequest(conceptDescriptionRequestMapper.map(safeGet(data, "description")));
        return request;
    }
}
