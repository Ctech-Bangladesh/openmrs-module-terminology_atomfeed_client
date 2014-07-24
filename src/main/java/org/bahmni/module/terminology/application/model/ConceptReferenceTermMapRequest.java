package org.bahmni.module.terminology.application.model;

public class ConceptReferenceTermMapRequest {

    private String uuid;
    private String display;
    private ConceptMapTerm termA;
    private ConceptMapTerm termB;

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

    public ConceptMapTerm getTermA() {
        return termA;
    }

    public void setTermA(ConceptMapTerm termA) {
        this.termA = termA;
    }

    public ConceptMapTerm getTermB() {
        return termB;
    }

    public void setTermB(ConceptMapTerm termB) {
        this.termB = termB;
    }

    public static class ConceptMapTerm {
        private String uuid;
        private String display;

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
    }
}
