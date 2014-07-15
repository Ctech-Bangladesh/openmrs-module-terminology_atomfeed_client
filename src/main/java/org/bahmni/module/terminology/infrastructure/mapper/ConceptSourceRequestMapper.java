package org.bahmni.module.terminology.infrastructure.mapper;

import org.bahmni.module.terminology.application.model.ConceptSourceRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptSourceRequestMapper {

    public ConceptSourceRequest map(Map data) {
        if (null != data) {
            ConceptSourceRequest conceptSourceRequest = new ConceptSourceRequest();
            String display = data.get("display").toString();
            conceptSourceRequest.setName(display);
            conceptSourceRequest.setHl7Code(display);
            conceptSourceRequest.setDescription(display);
            conceptSourceRequest.setUuid(asString(safeGet(data, "uuid")));
            return conceptSourceRequest;
        } else {
            return null;
        }
    }
}
