package org.bahmni.module.terminology.application.model;

public class ConceptNameRequest {

    private String conceptName;
    private String locale;
    private String conceptNameType;

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getConceptNameType() {
        return conceptNameType;
    }

    public void setConceptNameType(String conceptNameType) {
        this.conceptNameType = conceptNameType;
    }
}
