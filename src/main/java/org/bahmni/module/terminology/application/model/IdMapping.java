package org.bahmni.module.terminology.application.model;

public class IdMapping {

    private long id;
    private String internalId;
    private String externalId;
    private String type;
    private String uri;

    public IdMapping() {
    }

    public IdMapping(String internalId, String externalId, String type, String uri) {
        this.internalId = internalId;
        this.externalId = externalId;
        this.type = type;
        this.uri = uri;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
