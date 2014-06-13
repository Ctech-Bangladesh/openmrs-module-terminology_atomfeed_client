package org.bahmni.module.terminology.application.model;

import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;

import java.util.List;

public class ConceptObject {

    private String uuid;
    private String display;
    private ConceptName conceptName;
    private List<ConceptName> conceptNames;
    private String set;
    private String version;
    private String retired;
    private ConceptClass conceptClass;
    private ConceptDatatype conceptDatatype;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ConceptName getConceptName() {
        return conceptName;
    }

    public void setConceptName(ConceptName conceptName) {
        this.conceptName = conceptName;
    }

    public List<ConceptName> getConceptNames() {
        return conceptNames;
    }

    public void setConceptNames(List<ConceptName> conceptNames) {
        this.conceptNames = conceptNames;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRetired() {
        return retired;
    }

    public void setRetired(String retired) {
        this.retired = retired;
    }

    public ConceptClass getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(ConceptClass conceptClass) {
        this.conceptClass = conceptClass;
    }

    public ConceptDatatype getConceptDatatype() {
        return conceptDatatype;
    }

    public void setConceptDatatype(ConceptDatatype conceptDatatype) {
        this.conceptDatatype = conceptDatatype;
    }
}
