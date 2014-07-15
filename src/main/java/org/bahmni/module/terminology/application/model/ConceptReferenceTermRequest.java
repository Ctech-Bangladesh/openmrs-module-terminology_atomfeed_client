package org.bahmni.module.terminology.application.model;

public class ConceptReferenceTermRequest {

    private String name;
    private String code;
    private String description;
    private ConceptSourceRequest conceptSourceRequest;
    private String mapType;
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasSource() {
        return null != conceptSourceRequest;
    }

    public ConceptSourceRequest getConceptSourceRequest() {
        return this.conceptSourceRequest;
    }

    public void setConceptSourceRequest(ConceptSourceRequest conceptSourceRequest) {
        this.conceptSourceRequest = conceptSourceRequest;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
