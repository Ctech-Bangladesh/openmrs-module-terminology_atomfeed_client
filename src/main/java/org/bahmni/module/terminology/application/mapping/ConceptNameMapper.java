package org.bahmni.module.terminology.application.mapping;

import org.bahmni.module.terminology.application.model.ConceptNameRequest;
import org.bahmni.module.terminology.application.model.ConceptNameRequests;
import org.openmrs.ConceptName;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptNameType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Component
public class ConceptNameMapper {

    public ConceptName map(ConceptNameRequest name) {
        ConceptName conceptName = new ConceptName();
        if (name != null && isNotBlank(name.getConceptName()) && isNotBlank(name.getLocale())) {
            conceptName.setName(name.getConceptName());
            conceptName.setLocale(new Locale(name.getLocale()));
            conceptName.setLocalePreferred(name.isPreferred());
            if (isNotBlank(name.getConceptNameType())) {
                conceptName.setConceptNameType(getConceptNameType(name));
            }
        }
        return conceptName;
    }

    private ConceptNameType getConceptNameType(ConceptNameRequest name) {
        if (name.getConceptNameType() == null) return null;
        for (ConceptNameType conceptNameType : ConceptNameType.values()) {
            if (conceptNameType.name().equals(name.getConceptNameType())) {
                return conceptNameType;
            }
        }
        return null;
    }

    public Set<ConceptName> map(ConceptNameRequests conceptNameRequests) {
        ConceptNameRequest fullySpecifiedName = null;
        Set<ConceptName> conceptNames = new HashSet<>();
        if (conceptNameRequests.isNotEmpty()) {
            for (ConceptNameRequest conceptNameRequest : conceptNameRequests.getConceptNameRequests()) {
                ConceptNameType conceptNameType = getConceptNameType(conceptNameRequest);
                if (ConceptNameType.FULLY_SPECIFIED.equals(conceptNameType)) {
                    fullySpecifiedName = conceptNameRequest;
                }
                conceptNames.add(map(conceptNameRequest));
            }
        }
        if (fullySpecifiedName == null || fullySpecifiedName.getLocale() == null) {
            throw new APIException("A concept must have a fully specified name and its locale cannot be null");
        }
//        else if (fullySpecifiedName.isVoided()) {
//            throw new APIException("Fully Specified name cannot be null or voided");
//        }
        return conceptNames;
    }
}
