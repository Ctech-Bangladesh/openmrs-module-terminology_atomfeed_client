package org.bahmni.module.terminology.application.mapper;

import org.bahmni.module.terminology.application.model.ConceptName;
import org.bahmni.module.terminology.application.model.ConceptObject;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
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
    public ConceptMapper(ConceptNameMapper conceptNameMapper) {
        this.conceptNameMapper = conceptNameMapper;
        this.conceptService = Context.getConceptService();
    }

    public ConceptObject map(SimpleObject simpleObject) {
        ConceptObject result = new ConceptObject();
        result.setUuid(simpleObject.get("uuid").toString());
        result.setDisplay(simpleObject.get("display").toString());
        result.setConceptName(mapConceptName((Map) simpleObject.get("name")));
        result.setConceptNames(mapConceptNames((Collection) simpleObject.get("names")));
        result.setRetired(simpleObject.get("retired").toString());
        result.setSet(simpleObject.get("set").toString());
        result.setVersion(simpleObject.get("version").toString());
        result.setConceptDatatype(mapDataType((Map) simpleObject.get("dataType")));
        result.setConceptClass(mapConceptClass((Map) simpleObject.get("conceptClass")));
        return result;
    }

    private ConceptClass mapConceptClass(Map conceptClass) {
        String name = conceptClass.get("display").toString();
        return conceptService.getConceptClassByName(name);
    }

    private ConceptDatatype mapDataType(Map dataType) {
        String name = dataType.get("display").toString();
        return conceptService.getConceptDatatypeByName(name);
    }

    private List<ConceptName> mapConceptNames(Collection names) {
        List<ConceptName> result = new ArrayList<>();
        for (Object name : names) {
            result.add(mapConceptName((Map) name));
        }
        return result;
    }

    private ConceptName mapConceptName(Map name) {
        return conceptNameMapper.map(name);
    }
}
