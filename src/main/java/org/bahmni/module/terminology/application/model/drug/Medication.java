package org.bahmni.module.terminology.application.model.drug;


import org.bahmni.module.terminology.application.model.CodeableConcept;
import org.bahmni.module.terminology.application.model.ResourceExtension;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"resourceType","name", "code", "product", "extension"})
public class Medication {
    private String resourceType = "Medication";
    private String name;
    private CodeableConcept code;
    private MedicationProduct product;
    private List<ResourceExtension> extension;

    public Medication(String name, CodeableConcept code, MedicationProduct product) {
        this.name = name;
        this.code = code;
        this.product = product;
        this.extension = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public CodeableConcept getCode() {
        return code;
    }

    public MedicationProduct getProduct() {
        return product;
    }

    public String getResourceType() {
        return resourceType;
    }

    public List<ResourceExtension> getExtension() {
        return extension;
    }

    public void addExtension(ResourceExtension extn) {
        extension.add(extn);
    }
}