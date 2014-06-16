package org.bahmni.module.terminology.application.model;


import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;
import java.util.Map;

public class ConceptRestResource {

    private SimpleObject simpleObject;

    public ConceptRestResource(SimpleObject simpleObject) throws IOException {
        verifyRequiredPropertiesExist(simpleObject);
        this.simpleObject = removeEmptyProperties(simpleObject);
    }

    private void verifyRequiredPropertiesExist(SimpleObject simpleObject) {
        if (null == simpleObject.get("datatype")) throw new IllegalArgumentException("datatype is null");
        if (null == simpleObject.get("conceptClass")) throw new IllegalArgumentException("concept class is null");
    }

    private SimpleObject removeEmptyProperties(SimpleObject simpleObject) {
        SimpleObject result = new SimpleObject();
        for (Map.Entry<String, Object> entry : simpleObject.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public SimpleObject toDTO() {
        clearUnsupportedProperties();
        return simpleObject;
    }

    private void clearUnsupportedProperties() {
        simpleObject.removeProperty("uuid");
        simpleObject.removeProperty("precise");
        simpleObject.removeProperty("auditInfo");
        simpleObject.removeProperty("mappings");
    }

}
