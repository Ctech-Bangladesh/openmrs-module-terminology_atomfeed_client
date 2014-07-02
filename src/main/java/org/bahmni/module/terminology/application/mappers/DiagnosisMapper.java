package org.bahmni.module.terminology.application.mappers;

import org.openmrs.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiagnosisMapper implements Mapper<Concept> {
    @Autowired
    private ConceptDataTypeMapper dataTypeMapper;
    @Autowired
    private ConceptNameMapper nameMapper;
    @Autowired
    private ConceptReferenceTermMapper referenceTermMapper;
    @Autowired
    private DiagnosisConceptClassMapper conceptClassMapper;


    public Concept map(Map<String, Object> data) {
        return new ConceptMapper(nameMapper, referenceTermMapper, dataTypeMapper, conceptClassMapper).map(data);
    }

}
