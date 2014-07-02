package org.openmrs.integration;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;

import java.util.Locale;

public class ConceptReferenceData {

    public static ConceptSource conceptSource(String code, String name) {
        ConceptSource conceptSource = new ConceptSource();
        conceptSource.setName(name);
        conceptSource.setHl7Code(code);
        return conceptSource;
    }

    public static Concept concept(String name) {
        Concept concept = new Concept();
        concept.setFullySpecifiedName(new ConceptName(name, Locale.ENGLISH));
        return concept;
    }

    public static ConceptReferenceTerm referenceTerm(String code, ConceptSource conceptSource) {
        ConceptReferenceTerm referenceTerm = new ConceptReferenceTerm();
        referenceTerm.setConceptSource(conceptSource);
        referenceTerm.setCode(code);
        return referenceTerm;
    }

}
