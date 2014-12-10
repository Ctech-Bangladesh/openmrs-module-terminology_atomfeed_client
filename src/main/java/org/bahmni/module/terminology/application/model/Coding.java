package org.bahmni.module.terminology.application.model;


public class Coding {
    private String system;
    private String code;
    private String display;

    public Coding(String system, String code, String display) {
        this.code = code;
        this.display = display;
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public String getCode() {
        return code;
    }

    public String getDisplay() {
        return display;
    }
}
