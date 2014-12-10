package org.bahmni.module.terminology.application.model;



import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import java.util.ArrayList;
import java.util.List;

@JsonWriteNullProperties(false)
public class CodeableConcept {

    private List<Coding> coding = new ArrayList<>();
    private String text;

    public CodeableConcept(String text) {
        this.text = text;
    }

    public void addCoding(Coding code) {
        this.coding.add(code);
    }

    public List<Coding> getCoding() {
        return coding;
    }

    public String getText() {
        return text;
    }
}
