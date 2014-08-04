package org.bahmni.module.terminology.application.model;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptName;

import java.util.Locale;

public class PersistedConcept {

    private Concept existingConcept;

    public PersistedConcept(Concept existingConcept) {
        this.existingConcept = existingConcept;
    }

    public Concept merge(Concept newConcept) {
        existingConcept.setRetired(newConcept.isRetired());
        existingConcept.setRetireReason(newConcept.getRetireReason());
        existingConcept.setSet(newConcept.isSet());
        existingConcept.setVersion(newConcept.getVersion());
        existingConcept.setDatatype(newConcept.getDatatype());
        existingConcept.setConceptClass(newConcept.getConceptClass());
        mergeFullySpecifiedName(existingConcept, newConcept);
        mergeConceptNames(existingConcept, new ConceptNames(newConcept.getNames()));
        mergeConceptMappings(existingConcept, new ConceptMappings(newConcept.getConceptMappings()));
        mergeConceptDescriptions(existingConcept, newConcept);
        mergeConceptSets(existingConcept, newConcept);
        return existingConcept;
    }

    private void mergeConceptSets(Concept existingConcept, Concept newConcept) {
        if (null != newConcept.getSetMembers()) {
            for (Concept concept : newConcept.getSetMembers()) {
                if (findSetMember(existingConcept, concept)) {
                    existingConcept.addSetMember(newConcept);
                } else {
                    existingConcept.getSetMembers().remove(concept);
                }
            }
        }
    }

    private boolean findSetMember(Concept existingConcept, Concept concept) {
        if (null == existingConcept.getSetMembers()) {
            return false;
        }
        for (Concept member : existingConcept.getSetMembers()) {
            if (member.getUuid().equals(concept.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private void mergeConceptDescriptions(Concept existingConcept, Concept newConcept) {
        if (existingConcept.getDescription() == null && newConcept.getDescription() == null) {
            return;
        } else if (existingConcept.getDescription() == null) {
            existingConcept.addDescription(newConcept.getDescription());
        } else if (newConcept.getDescription() == null) {
            existingConcept.removeDescription(existingConcept.getDescription());
        } else if (!StringUtils.equals(existingConcept.getDescription().getDescription(), newConcept.getDescription().getDescription())) {
            existingConcept.getDescription().setDescription(newConcept.getDescription().getDescription());
        }
    }

    /*Assumes that locale does not change*/
    private void mergeFullySpecifiedName(Concept existingConcept, Concept newConcept) {
        for (Locale locale : existingConcept.getAllConceptNameLocales()) {
            existingConcept.getFullySpecifiedName(locale).setName(newConcept.getFullySpecifiedName(locale).getName());
        }
    }

    private void mergeConceptMappings(Concept existingConcept, ConceptMappings conceptMappings) {
        ConceptMappings existingMappings = new ConceptMappings(existingConcept.getConceptMappings());
        for (ConceptMap map : conceptMappings) {
            ConceptMap existingMap = existingMappings.findConceptMap(map);
            if (null == existingMap) {
                existingConcept.addConceptMapping(map);
            } else {
                existingMap.setConceptMapType(map.getConceptMapType());
                existingMap.getConceptReferenceTerm().setName(map.getConceptReferenceTerm().getName());
            }
        }
        for (ConceptMap existingMapping : existingMappings) {
            ConceptMap conceptMap = conceptMappings.findConceptMap(existingMapping);
            if (null == conceptMap) {
                existingConcept.removeConceptMapping(existingMapping);
            }
        }
    }

    private void mergeConceptNames(Concept existingConcept, ConceptNames newNames) {
        ConceptNames existingNames = new ConceptNames(existingConcept.getNames());
        for (ConceptName newName : newNames) {
            ConceptName existingConceptName = existingNames.findConceptName(newName);
            if (null == existingConceptName) {
                existingConcept.addName(newName);
            }
        }
        for (ConceptName existingName : existingNames) {
            ConceptName conceptName = newNames.findConceptName(existingName);
            if (null == conceptName) {
                existingName.setVoided(Boolean.TRUE);
            }
        }
    }
}
