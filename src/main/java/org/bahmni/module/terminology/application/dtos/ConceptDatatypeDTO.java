package org.bahmni.module.terminology.application.dtos;


public class ConceptDatatypeDTO {
    private Integer conceptDatatypeId;

    public ConceptDatatypeDTO() {

    }

    public ConceptDatatypeDTO(Integer conceptDatatypeId) {
        this.conceptDatatypeId = conceptDatatypeId;
    }

    public Integer getConceptDatatypeId() {
        return conceptDatatypeId;
    }

    public void setConceptDatatypeId(Integer conceptDatatypeId) {
        this.conceptDatatypeId = conceptDatatypeId;
    }
}
