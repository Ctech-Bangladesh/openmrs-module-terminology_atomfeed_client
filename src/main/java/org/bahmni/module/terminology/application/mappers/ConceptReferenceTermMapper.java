package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConceptReferenceTermMapper implements Mapper<ConceptMap> {

    private ConceptSourceMapper sourceMapper;
    private ConceptService conceptService;

    @Autowired
    public ConceptReferenceTermMapper(ConceptSourceMapper sourceMapper, ConceptService conceptService) {
        this.sourceMapper = sourceMapper;
        this.conceptService = conceptService;
    }

    public ConceptMap map(Map mappingData) {
        Map<String, Object> referenceTermData = (Map<String, Object>) mappingData.get("conceptReferenceTerm");
        Map<String, Object> conceptMapType = (Map<String, Object>) mappingData.get("conceptMapType");
        Map<String, Object> conceptSourceData = (Map<String, Object>) referenceTermData.get("conceptSource");
        if (conceptSourceData == null) {
            return null;
        }
        ConceptMap conceptMap = new ConceptMap();
        mapConceptMapType(conceptMapType, conceptMap);
        conceptMap.setConceptReferenceTerm(toReferenceTerm(referenceTermData, sourceMapper.map(conceptSourceData)));
        return conceptMap;
    }

    private void mapConceptMapType(Map<String, Object> conceptMapType, ConceptMap conceptMap) {
        if (conceptMapType != null && conceptMapType.get("name") != null) {
            Object name = conceptMapType.get("name");
            conceptMap.setConceptMapType(conceptService.getConceptMapTypeByName(name.toString()));
        }
    }

    private ConceptReferenceTerm toReferenceTerm(Map data, ConceptSource source) {
        String code = data.get("code").toString();
        String name = data.get("name").toString();
        String description = data.get("description").toString();

        ConceptReferenceTerm referenceTerm = conceptService.getConceptReferenceTermByCode(code, source);
        return referenceTerm != null ? referenceTerm : createNew(code, name, description, source);
    }

    private ConceptReferenceTerm createNew(String code, String name, String description, ConceptSource source) {
        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        referenceTerm.setName(name);
        referenceTerm.setCode(code);
        referenceTerm.setDescription(description);
        referenceTerm.setConceptSource(source);
        return conceptService.saveConceptReferenceTerm(referenceTerm);
    }

}
