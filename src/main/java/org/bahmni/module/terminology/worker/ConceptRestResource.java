package org.bahmni.module.terminology.worker;


import org.openmrs.api.ConceptService;
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

    public SimpleObject toDTO(ConceptService conceptService) {
        clearUnsupportedProperties();
        updateRefereces(conceptService);
        return simpleObject;
    }

    private void clearUnsupportedProperties() {
        simpleObject.removeProperty("uuid");
        simpleObject.removeProperty("precise");
        simpleObject.removeProperty("auditInfo");
    }

    private void updateRefereces(ConceptService conceptService) {
        getNestedMap("datatype").put("conceptDatatypeId", conceptService.getConceptDatatypeByUuid(dataTypeUUID()).getId());
        getNestedMap("conceptClass").put("conceptClassId", conceptService.getConceptClassByUuid(classUUID()).getId());
    }


    private Map getNestedMap(String key) {
        return ((Map) simpleObject.get(key));
    }


    private String dataTypeUUID() {
        return (String) ((Map) simpleObject.get("datatype")).get("uuid");
    }

    private String classUUID() {
        return (String) ((Map) simpleObject.get("conceptClass")).get("uuid");
    }

}
