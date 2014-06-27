package org.bahmni.module.terminology.application.mappers;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptName;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.bahmni.module.terminology.util.CollectionUtils.safeGet;

@Component
public class ConceptMapper {
    private ConceptNameMapper nameMapper;


    @Autowired
    public ConceptMapper(ConceptNameMapper nameMapper) {
        this.nameMapper = nameMapper;
    }

    public Concept map(Map<String, Object> data, ConceptService conceptService) {
        Concept concept = new Concept();
        concept.setFullySpecifiedName(nameMapper.map((Map) data.get("name")));
        concept.setNames(toNames(data.get("names")));
        concept.setSet(Boolean.valueOf(safeGet(data, "set", "false").toString()));
        concept.setVersion(safeGet(data, "resourceVersion", StringUtils.EMPTY).toString());
        concept.setDatatype(toDataType(data, conceptService));
        concept.setConceptClass(toConceptClass(data, conceptService));
        return concept;
    }

    private ConceptClass toConceptClass(Map<String, Object> data, ConceptService conceptService) {
        if (data.containsKey("conceptClass")) {
            Map conceptClass = (Map) data.get("conceptClass");
            return conceptService.getConceptClassByName(conceptClass.get("name").toString());
        }
        return null;
    }

    private ConceptDatatype toDataType(Map<String, Object> data, ConceptService conceptService) {
        if (data.containsKey("datatype")) {
            Map dataType = (Map) data.get("datatype");
            return conceptService.getConceptDatatypeByName(dataType.get("name").toString());
        }
        return null;
    }

    private Set<ConceptName> toNames(Object names) {
        Set<ConceptName> conceptNames = new HashSet<ConceptName>();
        if (conceptNames != null) {
            for (Object name : (Collection) names) {
                conceptNames.add(nameMapper.map((Map) name));
            }
        }
        return conceptNames;
    }
}
