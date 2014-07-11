package org.bahmni.module.terminology.application.model;

public class ConceptRequest {

    private boolean isSet;
    private String version;
    private String datatypeName;
    private String conceptClass;
    private ConceptNameRequest fullySpecifiedName;
    private ConceptNameRequests conceptNameRequests;
    private ConceptReferenceTermRequests conceptReferenceTermRequests;

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean isSet) {
        this.isSet = isSet;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDatatypeName() {
        return datatypeName;
    }

    public void setDatatypeName(String datatypeName) {
        this.datatypeName = datatypeName;
    }

    public String getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(String conceptClass) {
        this.conceptClass = conceptClass;
    }

    public ConceptNameRequests getConceptNameRequests() {
        return conceptNameRequests;
    }

    public void setConceptNameRequests(ConceptNameRequests conceptNameRequests) {
        this.conceptNameRequests = conceptNameRequests;
    }

    public ConceptReferenceTermRequests getConceptReferenceTermRequests() {
        return conceptReferenceTermRequests;
    }

    public void setConceptReferenceTermRequests(ConceptReferenceTermRequests conceptReferenceTermRequests) {
        this.conceptReferenceTermRequests = conceptReferenceTermRequests;
    }

    public ConceptNameRequest getFullySpecifiedName() {
        return fullySpecifiedName;
    }

    public void setFullySpecifiedName(ConceptNameRequest fullySpecifiedName) {
        this.fullySpecifiedName = fullySpecifiedName;
    }
}
