package org.bahmni.module.terminology.application.model.drug;


import org.bahmni.module.terminology.application.model.CodeableConcept;

public class MedicationProduct {
    private CodeableConcept form = new CodeableConcept(null);

    public MedicationProduct(CodeableConcept form) {
        this.form = form;
    }

    public CodeableConcept getForm() {
        return form;
    }
}
