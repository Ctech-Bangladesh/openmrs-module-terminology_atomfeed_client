package org.bahmni.module.terminology.worker;


import org.openmrs.api.ConceptService;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;
import java.util.Map;

public class ConceptRestResource {

    private SimpleObject simpleObject;

    public ConceptRestResource(SimpleObject simpleObject) throws IOException {
        SimpleObject result = new SimpleObject();
        for (Map.Entry<String, Object> entry : simpleObject.entrySet()) {
            if (entry.getValue() != null) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        this.simpleObject = result;
    }

    public SimpleObject toDTO(ConceptService conceptService) {
        clearUnsupportedProperties();
        updateRefereces(conceptService);
        return simpleObject;
    }

    public String uuid() {
        return (String) simpleObject.get("uuid");
    }

    private void clearUnsupportedProperties() {
        simpleObject.removeProperty("uuid");
        simpleObject.removeProperty("precise");
        simpleObject.removeProperty("auditInfo");
    }

    private void updateRefereces(ConceptService conceptService) {
        getProperty("datatype").put("conceptDatatypeId", conceptService.getConceptDatatypeByUuid(dataTypeUUID()));
        getProperty("conceptClass").put("classID", conceptService.getConceptClassByUuid(classUUID()));
    }


    private Map getProperty(String key) {
        return ((Map) simpleObject.get(key));
    }


    private String dataTypeUUID() {
        return (String) ((Map) simpleObject.get("datatype")).get("uuid");
    }

    private String classUUID() {
        return (String) ((Map) simpleObject.get("conceptClass")).get("uuid");
    }

}
