package org.bahmni.module.terminology.application.mapper;

import org.bahmni.module.terminology.application.model.ConceptClassDTO;
import org.bahmni.module.terminology.application.model.ConceptDatatypeDTO;
import org.bahmni.module.terminology.application.model.ConceptNameDTO;
import org.bahmni.module.terminology.application.model.ConceptDTO;
import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class ConceptMapper {

    private ConceptNameMapper conceptNameMapper;
    private ConceptService conceptService;

    @Autowired
    public ConceptMapper(ConceptNameMapper conceptNameMapper, ConceptService conceptService) {
        this.conceptNameMapper = conceptNameMapper;
        this.conceptService = conceptService;
    }

    public ConceptDTO map(SimpleObject simpleObject) {
        ConceptDTO result = new ConceptDTO();
        result.setUuid(simpleObject.get("uuid").toString());
        result.setDisplay(simpleObject.get("display").toString());
        result.setName(mapConceptName((Map) simpleObject.get("name")));
        result.setNames(mapConceptNames((Collection) simpleObject.get("names")));
        result.setSet(simpleObject.get("set").toString());
        result.setVersion(simpleObject.get("resourceVersion").toString());
        result.setDatatype(mapDataType((Map) simpleObject.get("datatype")));
        result.setConceptClass(mapConceptClass((Map) simpleObject.get("conceptClass")));
        return result;
    }

    private ConceptClassDTO mapConceptClass(Map conceptClass) {
        return new ConceptClassDTO(conceptService.getConceptClassByName(conceptClass.get("display").toString()).getId());
    }

    private ConceptDatatypeDTO mapDataType(Map dataType) {
        return new ConceptDatatypeDTO(conceptService.getConceptDatatypeByName(dataType.get("display").toString()).getId());
    }

    private List<ConceptNameDTO> mapConceptNames(Collection names) {
        List<ConceptNameDTO> result = new ArrayList<>();
        for (Object name : names) {
            result.add(mapConceptName((Map) name));
        }
        return result;
    }

    private ConceptNameDTO mapConceptName(Map name) {
        return conceptNameMapper.map(name);
    }
}
