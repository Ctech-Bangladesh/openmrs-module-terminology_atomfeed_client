package org.bahmni.module.terminology.application.model;

import java.util.List;

public class ConceptObject {

    private String uuid;
    private String display;
    private ConceptName name;
    private List<ConceptName> names;
    private String set;
    private String version;
    private ConceptClassDTO conceptClass;
    private ConceptDatatypeDTO datatype;

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

    public ConceptName getName() {
        return name;
    }

    public void setName(ConceptName name) {
        this.name = name;
    }

    public List<ConceptName> getNames() {
        return names;
    }

    public void setNames(List<ConceptName> names) {
        this.names = names;
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

    public ConceptClassDTO getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(ConceptClassDTO conceptClass) {
        this.conceptClass = conceptClass;
    }

    public ConceptDatatypeDTO getDatatype() {
        return datatype;
    }

    public void setDatatype(ConceptDatatypeDTO datatype) {
        this.datatype = datatype;
    }
}
