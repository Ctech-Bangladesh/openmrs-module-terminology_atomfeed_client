package org.bahmni.module.terminology.infrastructure.mapper;

import org.bahmni.module.terminology.application.model.ConceptNameRequest;
import org.bahmni.module.terminology.application.model.ConceptNameRequests;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptNameRequestMapper {

    public ConceptNameRequests map(Object data) {
        ConceptNameRequests result = new ConceptNameRequests();
        Collection conceptNames = (Collection) data;
        if (null != conceptNames) {
            for (Object name : conceptNames) {
                result.add(mapNameRequest((Map) name));
            }
        }
        return result;
    }

    public ConceptNameRequest mapNameRequest(Map nameRequest) {
        ConceptNameRequest conceptName = new ConceptNameRequest();
        if (nameRequest != null) {
            conceptName.setConceptName(safeGet(nameRequest, "name", EMPTY).toString());
            conceptName.setLocale(safeGet(nameRequest, "locale", "en").toString());
            conceptName.setConceptNameType(asString(safeGet(nameRequest, "conceptNameType", null)));
        }
        return conceptName;
    }
}
