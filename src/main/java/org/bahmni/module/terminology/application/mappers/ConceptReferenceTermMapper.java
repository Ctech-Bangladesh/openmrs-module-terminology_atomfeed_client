package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptMap;
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
        Map<String, Object> conceptSourceData = (Map<String, Object>) referenceTermData.get("conceptSource");
        if (conceptSourceData == null) {
            return null;
        }
        ConceptMap conceptMap = new ConceptMap();
        conceptMap.setConceptReferenceTerm(toReferenceTerm(referenceTermData, sourceMapper.map(conceptSourceData)));
        return conceptMap;
    }

    private ConceptReferenceTerm toReferenceTerm(Map data, ConceptSource source) {
        String code = data.get("code").toString();
        String name = data.get("name").toString();

        ConceptReferenceTerm referenceTerm = conceptService.getConceptReferenceTermByCode(code, source);
        return referenceTerm != null ? referenceTerm : createNew(code, name, source);
    }

    private ConceptReferenceTerm createNew(String code, String name, ConceptSource source) {
        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        referenceTerm.setName(name);
        referenceTerm.setCode(code);
        referenceTerm.setConceptSource(source);
        return conceptService.saveConceptReferenceTerm(referenceTerm);
    }

}
