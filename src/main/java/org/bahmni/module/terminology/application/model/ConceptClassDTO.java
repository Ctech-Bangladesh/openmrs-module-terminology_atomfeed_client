package org.bahmni.module.terminology.application.model;


public class ConceptClassDTO {
    private Integer conceptClassId;


    public ConceptClassDTO() {
    }

    public ConceptClassDTO(Integer conceptClassId) {
        this.conceptClassId = conceptClassId;
    }

    public Integer getConceptClassId() {
        return conceptClassId;
    }

    public void setConceptClassId(Integer conceptClassId) {
        this.conceptClassId = conceptClassId;
    }
}
