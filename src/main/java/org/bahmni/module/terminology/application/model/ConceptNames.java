package org.bahmni.module.terminology.application.model;

import org.openmrs.ConceptName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ConceptNames implements Iterable<ConceptName> {

    private Collection<ConceptName> conceptNames;

    public ConceptNames(Collection<ConceptName> conceptNames) {
        this.conceptNames = conceptNames;
    }

    @Override
    public Iterator<ConceptName> iterator() {
        if (conceptNames != null) {
            return conceptNames.iterator();
        } else {
            return new ArrayList<ConceptName>().iterator();
        }
    }

    public ConceptName findConceptName(ConceptName conceptName) {
        if (conceptNames == null) {
            return null;
        }
        for (ConceptName name : conceptNames) {
            if (name.getName().equals(conceptName.getName())) {
                return name;
            }
        }
        return null;
    }

}
