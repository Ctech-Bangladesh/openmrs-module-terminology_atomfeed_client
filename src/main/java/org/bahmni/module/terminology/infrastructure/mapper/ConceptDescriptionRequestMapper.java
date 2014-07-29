package org.bahmni.module.terminology.infrastructure.mapper;


import org.bahmni.module.terminology.application.model.ConceptDescriptionRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.bahmni.module.terminology.util.CollectionUtil.safeGet;
import static org.bahmni.module.terminology.util.TypeUtil.asString;

@Component
public class ConceptDescriptionRequestMapper {

    public ConceptDescriptionRequest map(Object conceptDescriptionRequest) {
        if (null == conceptDescriptionRequest) {
            return null;
        }
        ConceptDescriptionRequest request = new ConceptDescriptionRequest();
        request.setDescription(asString(safeGet((Map) conceptDescriptionRequest, "description")));
        return request;
    }
}
