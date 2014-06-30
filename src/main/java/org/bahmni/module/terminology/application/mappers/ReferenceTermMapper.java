package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReferenceTermMapper {

    public ConceptMap map(Map mapping, ConceptService conceptService) {
        ConceptSource conceptSource = getConceptSource(mapping, conceptService);
        if (conceptSource != null) {
            ConceptMap conceptMap = new ConceptMap();
            conceptMap.setConceptReferenceTerm(toReferenceTerm(mapping, conceptSource, conceptService));
            return conceptMap;
        }
        return null;
    }

    private ConceptSource getConceptSource(Map mapping, ConceptService conceptService) {
        Map<String, Object> referenceTermData = (Map<String, Object>) mapping.get("conceptReferenceTerm");
        Map<String, Object> conceptSourceData = (Map<String, Object>) referenceTermData.get("conceptSource");
        return conceptService.getConceptSourceByName(conceptSourceData.get("display").toString());
    }


    private ConceptReferenceTerm toReferenceTerm(Map mapping, ConceptSource conceptSource, ConceptService conceptService) {
        Map<String, Object> referenceTermData = (Map<String, Object>) mapping.get("conceptReferenceTerm");
        String conceptRefTermCode = referenceTermData.get("code").toString();
        if (conceptService.getConceptReferenceTermByCode(conceptRefTermCode, conceptSource) != null) {
            return conceptService.getConceptReferenceTermByCode(conceptRefTermCode, conceptSource);
        }

        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        referenceTerm.setCode(conceptRefTermCode);
        referenceTerm.setName(referenceTermData.get("name").toString());
        referenceTerm.setConceptSource(conceptSource);
        return referenceTerm;
    }
}
