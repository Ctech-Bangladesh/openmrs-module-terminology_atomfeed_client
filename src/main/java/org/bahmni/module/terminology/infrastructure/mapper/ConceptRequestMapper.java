package org.bahmni.module.terminology.infrastructure.mapper;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.CollectionUtil.safeGetMap;
import static org.bahmni.module.terminology.util.TypeUtil.*;

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
        request.setFullySpecifiedName(conceptNameRequestMapper.mapNameRequest(safeGetMap(data, "fullySpecifiedName")));
        request.setConceptClass(asString(safeGet(data, "conceptClass", StringUtils.EMPTY)));
        request.setDatatypeName(asString(safeGet(data, "datatypeName", StringUtils.EMPTY)));
        request.setSet(Boolean.valueOf(asString(safeGet(data, "set", "false"))));
        request.setRetired(Boolean.valueOf(asString(safeGet(data, "retired", "false"))));
        request.setRetireReason(asString(safeGet(data, "retireReason", StringUtils.EMPTY)));
        request.setVersion(asString(safeGet(data, "version", StringUtils.EMPTY)));
        request.setConceptNameRequests(conceptNameRequestMapper.map(data.get("names")));
        request.setConceptReferenceTermRequests(conceptReferenceTermRequestMapper.mapFromConceptRequest(data.get("referenceTerms")));
        request.setUuid(asString(safeGet(data, "uuid")));
        request.setUri(asString(safeGet(data, "uri")));
        request.setConceptDescriptionRequest(conceptDescriptionRequestMapper.map(safeGet(data, "description")));
        mapSetMembers(request, data);
        mapConceptAnswers(request, data);
        mapNumericFields(request, data);
        return request;
    }

    private void mapConceptAnswers(ConceptRequest request, Map<String, Object> data) {
        Collection conceptAnswers = (Collection) safeGet(data, "conceptAnswers");
        if (null != conceptAnswers) {
            ArrayList<String> conceptAnswerUuids = new ArrayList<>();
            for (Object conceptAnswer : conceptAnswers) {
                conceptAnswerUuids.add(asString(((Map) conceptAnswer).get("uuid")));
            }
            request.setConceptAnswers(conceptAnswerUuids);
        }
    }

    private void mapNumericFields(ConceptRequest request, Map<String, Object> data) {
        request.setAbsoluteHigh(asDouble(safeGet(safeGetMap(data, "properties"), "ABSOLUTE_HIGH")));
        request.setCriticalHigh(asDouble(safeGet(safeGetMap(data, "properties"), "CRITICAL_HIGH")));
        request.setNormalHigh(asDouble(safeGet(safeGetMap(data, "properties"), "NORMAL_HIGH")));
        request.setNormalLow(asDouble(safeGet(safeGetMap(data, "properties"), "NORMAL_LOW")));
        request.setCriticalLow(asDouble(safeGet(safeGetMap(data, "properties"), "CRITICAL_LOW")));
        request.setAbsoluteLow(asDouble(safeGet(safeGetMap(data, "properties"), "ABSOLUTE_LOW")));
        request.setPrecise(asBoolean(safeGet(safeGetMap(data, "properties"), "NUMERIC_PRECISE")));
        request.setUnits(asString(safeGet(safeGetMap(data, "properties"), "NUMERIC_UNITS")));
    }

    private void mapSetMembers(ConceptRequest request, Map<String, Object> data) {
        Collection setMembers = (Collection) safeGet(data, "setMembers");
        if (null != setMembers) {
            ArrayList<String> setMemberUuids = new ArrayList<>();
            for (Object setMember : setMembers) {
                setMemberUuids.add(asString(((Map) setMember).get("uuid")));

            }
            request.setSetMembers(new ArrayList<String>(setMemberUuids));
        }
    }
}
