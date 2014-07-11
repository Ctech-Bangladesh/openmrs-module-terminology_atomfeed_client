package org.bahmni.module.terminology.infrastructure.mapper;

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

    @Autowired
    public ConceptReferenceTermRequestMapper(ConceptSourceRequestMapper conceptSourceRequestMapper) {
        this.conceptSourceRequestMapper = conceptSourceRequestMapper;
    }

    public ConceptReferenceTermRequests map(Object data) {
        ConceptReferenceTermRequests result = new ConceptReferenceTermRequests();
        Collection conceptNames = (Collection) data;
        if (null != conceptNames) {
            for (Object name : conceptNames) {
                result.add(mapReferenceTerm((Map) name));
            }
        }
        return result;
    }

    private ConceptReferenceTermRequest mapReferenceTerm(Map name) {
        ConceptReferenceTermRequest conceptReferenceTermRequest = new ConceptReferenceTermRequest();
        conceptReferenceTermRequest.setCode(asString(safeGet(safeGetMap(name, "conceptReferenceTerm"), "code")));
        conceptReferenceTermRequest.setName(asString(safeGet(safeGetMap(name, "conceptReferenceTerm"), "name")));
        conceptReferenceTermRequest.setDescription(asString(safeGet(safeGetMap(name, "conceptReferenceTerm"), "description")));
        conceptReferenceTermRequest.setMapType(asString(safeGet(safeGetMap(name, "conceptMapType"), "name")));
        conceptReferenceTermRequest.setConceptSourceRequest(conceptSourceRequestMapper.map(safeGetMap(safeGetMap(name, "conceptReferenceTerm"), "conceptSource")));
        return conceptReferenceTermRequest;
    }
}
