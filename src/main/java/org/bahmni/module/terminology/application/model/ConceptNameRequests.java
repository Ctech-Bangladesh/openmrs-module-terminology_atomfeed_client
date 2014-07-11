package org.bahmni.module.terminology.application.model;


import java.util.ArrayList;
import java.util.List;

public class ConceptNameRequests {

    private List<ConceptNameRequest> conceptNameRequests = new ArrayList<>();

    public void add(ConceptNameRequest conceptNameRequest) {
        conceptNameRequests.add(conceptNameRequest);
    }

    public List<ConceptNameRequest> getConceptNameRequests() {
        return conceptNameRequests;
    }

    public boolean isNotEmpty() {
        return !conceptNameRequests.isEmpty();
    }
}
