package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptName;
import org.openmrs.api.ConceptNameType;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.bahmni.module.terminology.util.CollectionUtils.safeGet;

@Component
public class ConceptNameMapper implements Mapper<ConceptName> {

    public ConceptName map(Map<String, Object> data) {
        ConceptName conceptName = new ConceptName();
        if (data != null) {
            conceptName.setName(safeGet(data, "name", EMPTY).toString());
            conceptName.setLocale(new Locale(safeGet(data, "locale", "en").toString()));
            Object conceptNameType = data.get("conceptNameType");
            if (conceptNameType != null && isNotEmpty(conceptNameType.toString())) {
                conceptName.setConceptNameType(ConceptNameType.valueOf(conceptNameType.toString()));
            }
        }
        return conceptName;
    }
}
