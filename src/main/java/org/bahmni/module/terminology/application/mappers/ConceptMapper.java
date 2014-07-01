package org.bahmni.module.terminology.application.mappers;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.collections.CollectionUtils.addIgnoreNull;
import static org.bahmni.module.terminology.util.CollectionUtils.safeGet;
import static org.bahmni.module.terminology.util.CollectionUtils.safeGetMap;

public class ConceptMapper {
    private Mapper<ConceptDatatype> dataTypeMapper;
    private Mapper<ConceptName> nameMapper;
    private Mapper<ConceptMap> referenceTermMapper;
    private Mapper<ConceptClass> conceptClassMapper;

    public ConceptMapper(Mapper<ConceptName> nameMapper,
                         Mapper<ConceptMap> referenceTermMapper,
                         Mapper<ConceptDatatype> dataTypeMapper,
                         Mapper<ConceptClass> conceptClassMapper) {
        this.nameMapper = nameMapper;
        this.referenceTermMapper = referenceTermMapper;
        this.dataTypeMapper = dataTypeMapper;
        this.conceptClassMapper = conceptClassMapper;
    }

    public Concept map(Map<String, Object> data) {
        Concept concept = new Concept();
        concept.setFullySpecifiedName(nameMapper.map((Map) data.get("name")));
        concept.setSet(Boolean.valueOf(safeGet(data, "set", "false").toString()));
        concept.setVersion(safeGet(data, "resourceVersion", StringUtils.EMPTY).toString());
        concept.setDatatype(dataTypeMapper.map(safeGetMap(data, "datatype")));
        concept.setConceptClass(conceptClassMapper.map(safeGetMap(data, "conceptClass")));
        concept.setNames(toNames(data.get("names")));
        concept.setConceptMappings(toConceptMappings(data));
        return concept;
    }

    private Collection<ConceptMap> toConceptMappings(Map<String, Object> data) {
        Set<ConceptMap> conceptMappings = new HashSet<ConceptMap>();
        if (data.containsKey("mappings")) {
            for (Object mapping : (Collection) data.get("mappings")) {
                addIgnoreNull(conceptMappings, referenceTermMapper.map((Map) mapping));
            }
        }
        return conceptMappings;
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
