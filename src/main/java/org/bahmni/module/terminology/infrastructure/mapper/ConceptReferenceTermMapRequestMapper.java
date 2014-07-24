package org.bahmni.module.terminology.infrastructure.mapper;

import org.bahmni.module.terminology.application.model.ConceptReferenceTermMapRequest;
import org.bahmni.module.terminology.application.model.ConceptReferenceTermMapRequests;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptReferenceTermMapRequestMapper {

    public ConceptReferenceTermMapRequests map(Collection<Object> data) {
        ConceptReferenceTermMapRequests result = new ConceptReferenceTermMapRequests();
        for (Object datum : data) {
            ConceptReferenceTermMapRequest request = new ConceptReferenceTermMapRequest();
            request.setUuid(asString(safeGet((Map) datum, "uuid")));
            request.setDisplay(asString(safeGet((Map) datum, "display")));
            request.setTermA(mapTerm((Map) safeGet((Map) datum, "termA")));
            request.setTermB(mapTerm((Map) safeGet((Map) datum, "termB")));
            result.add(request);
        }
        return result;
    }

    private ConceptReferenceTermMapRequest.ConceptMapTerm mapTerm(Map termData) {
        ConceptReferenceTermMapRequest.ConceptMapTerm term = new ConceptReferenceTermMapRequest.ConceptMapTerm();
        term.setUuid(asString(safeGet(termData, "uuid")));
        term.setDisplay(asString(safeGet(termData, "display")));
        return term;
    }
}
