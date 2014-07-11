package org.bahmni.module.terminology.application.model;


import java.util.ArrayList;
import java.util.List;

public class ConceptReferenceTermRequests {

    private List<ConceptReferenceTermRequest> conceptReferenceTermRequests = new ArrayList<>();

    public void add(ConceptReferenceTermRequest conceptReferenceTermRequest) {
        conceptReferenceTermRequests.add(conceptReferenceTermRequest);
    }

    public List<ConceptReferenceTermRequest> getConceptReferenceTermRequests() {
        return conceptReferenceTermRequests;
    }

    public boolean isNotEmpty() {
        return !conceptReferenceTermRequests.isEmpty();
    }
}
