package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptNameRequest;
import org.bahmni.module.terminology.application.model.ConceptNameRequests;
import org.openmrs.ConceptName;
import org.openmrs.api.ConceptNameType;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class ConceptNameMapper {

    public ConceptName map(ConceptNameRequest name) {
        ConceptName conceptName = new ConceptName();
        if (name != null) {
            conceptName.setName(name.getConceptName());
            conceptName.setLocale(new Locale(name.getLocale()));
            if (isNotBlank(name.getConceptNameType())) {
                conceptName.setConceptNameType(ConceptNameType.valueOf(name.getConceptNameType()));
            }
        }
        return conceptName;
    }

    public Set<ConceptName> map(ConceptNameRequests conceptNameRequests) {
        Set<ConceptName> conceptNames = new HashSet<>();
        if (conceptNameRequests.isNotEmpty()) {
            for (ConceptNameRequest conceptNameRequest : conceptNameRequests.getConceptNameRequests()) {
                conceptNames.add(map(conceptNameRequest));
            }
        }
        return conceptNames;
    }
}
