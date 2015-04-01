package org.bahmni.module.terminology.application.postprocessor;

import org.bahmni.module.terminology.infrastructure.atomfeed.postprocessors.ConceptPostProcessor;
import org.openmrs.Concept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostProcessorFactory {

    private List<ConceptPostProcessor> postProcessors;

    @Autowired
    public PostProcessorFactory(List<ConceptPostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public List<ConceptPostProcessor> getPostProcessors(Concept savedConcept) {
        List<ConceptPostProcessor> matchingPostProcessors = new ArrayList<>();
        for (ConceptPostProcessor postProcessor : postProcessors) {
            if(postProcessor.getConceptType().matches(savedConcept.getConceptClass().getName())){
                matchingPostProcessors.add(postProcessor);
            }
        }
        return matchingPostProcessors;
    }
}
