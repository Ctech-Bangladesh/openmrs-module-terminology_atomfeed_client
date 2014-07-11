package org.bahmni.module.terminology.application.model;

public enum ConceptType {

    Diagnosis("Diagnosis");
    private String conceptName;

    ConceptType(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getConceptName() {
        return conceptName;
    }
}
