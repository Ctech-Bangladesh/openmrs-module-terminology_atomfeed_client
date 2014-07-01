package org.bahmni.module.terminology.application.mappers;

import org.openmrs.ConceptSource;
import org.openmrs.api.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConceptSourceMapper implements Mapper<ConceptSource>{

    private ConceptService conceptService;

    @Autowired
    public ConceptSourceMapper(ConceptService conceptService){
        this.conceptService = conceptService;
    }

    public ConceptSource map(Map<String, Object> data) {
        ConceptSource conceptSource = conceptService.getConceptSourceByName(data.get("display").toString());
        return conceptSource != null ? conceptSource : createNew(data, conceptService);
    }

    private ConceptSource createNew(Map<String, Object> data, ConceptService conceptService) {
        ConceptSource conceptSource = new ConceptSource();
        String conceptSourceCode = data.get("display").toString();
        conceptSource.setName(conceptSourceCode);
        conceptSource.setHl7Code(conceptSourceCode);
        conceptSource.setDescription(conceptSourceCode);
        return conceptService.saveConceptSource(conceptSource);
    }
}
