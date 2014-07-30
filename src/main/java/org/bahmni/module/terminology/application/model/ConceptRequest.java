package org.bahmni.module.terminology.application.model;

public class ConceptRequest {

    private String uuid;
    private String version;
    private String datatypeName;
    private String conceptClass;
    private boolean isSet;
    private boolean isRetired;
    private ConceptNameRequest fullySpecifiedName;
    private ConceptNameRequests conceptNameRequests;
    private ConceptReferenceTermRequests conceptReferenceTermRequests;
    private ConceptDescriptionRequest conceptDescriptionRequest;

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean isSet) {
        this.isSet = isSet;
    }

    public boolean isRetired() {
        return isRetired;
    }

    public void setRetired(boolean isRetired) {
        this.isRetired = isRetired;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ConceptDescriptionRequest getConceptDescriptionRequest() {
        return conceptDescriptionRequest;
    }

    public void setConceptDescriptionRequest(ConceptDescriptionRequest conceptDescriptionRequest) {
        this.conceptDescriptionRequest = conceptDescriptionRequest;
    }
}
