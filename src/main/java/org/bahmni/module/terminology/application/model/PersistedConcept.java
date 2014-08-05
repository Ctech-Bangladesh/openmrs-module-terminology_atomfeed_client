package org.bahmni.module.terminology.application.model;

import org.apache.commons.lang.StringUtils;
import org.openmrs.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

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
        mapConceptDataType(newConcept);
        existingConcept.setConceptClass(newConcept.getConceptClass());
        mergeFullySpecifiedName(existingConcept, newConcept);
        mergeConceptNames(existingConcept, new ConceptNames(newConcept.getNames()));
        mergeConceptMappings(existingConcept, new ConceptMappings(newConcept.getConceptMappings()));
        mergeConceptDescriptions(existingConcept, newConcept);
        mergeConceptSets(existingConcept, newConcept);
        return existingConcept;
    }

    private void mapConceptDataType(Concept newConcept) {
        existingConcept.setDatatype(newConcept.getDatatype());
        if (newConcept.isNumeric()) {
            existingConcept = new ConceptNumeric(existingConcept);
            ConceptNumeric conceptNumeric = ((ConceptNumeric) existingConcept);
            conceptNumeric.setHiAbsolute(((ConceptNumeric) newConcept).getHiAbsolute());
            conceptNumeric.setHiCritical(((ConceptNumeric) newConcept).getHiCritical());
            conceptNumeric.setHiNormal(((ConceptNumeric) newConcept).getHiNormal());
            conceptNumeric.setLowNormal(((ConceptNumeric) newConcept).getLowNormal());
            conceptNumeric.setLowCritical(((ConceptNumeric) newConcept).getLowCritical());
            conceptNumeric.setLowAbsolute(((ConceptNumeric) newConcept).getLowAbsolute());
            conceptNumeric.setUnits(((ConceptNumeric) newConcept).getUnits());
            conceptNumeric.setPrecise(((ConceptNumeric) newConcept).getPrecise());
        }
    }

    private void mergeConceptSets(Concept existingConcept, Concept newConcept) {
        if (isNotEmpty(newConcept.getSetMembers())) {
            for (Concept concept : newConcept.getSetMembers()) {
                Concept setMember = findSetMember(existingConcept, concept);
                if (null == setMember) {
                    existingConcept.addSetMember(newConcept);
                }
            }
        } else if (isNotEmpty(existingConcept.getSetMembers())) {
            for (Concept concept : existingConcept.getSetMembers()) {
                Concept setMember = findSetMember(newConcept, concept);
                if (null == setMember) {
                    removeConceptSet(existingConcept.getConceptSets(), concept);
                }
            }
        }
    }

    private void removeConceptSet(Collection<ConceptSet> conceptSets, Concept existingConcept) {
        for (ConceptSet conceptSet : new ArrayList<>(conceptSets)) {
            if (conceptSet.getConcept().getUuid().equals(existingConcept.getUuid())) {
                conceptSets.remove(conceptSet);
            }
        }
    }

    private Concept findSetMember(Concept existingConcept, Concept concept) {
        if (null == existingConcept.getSetMembers()) {
            return null;
        }
        for (Concept member : existingConcept.getSetMembers()) {
            if (member.getUuid().equals(concept.getUuid())) {
                return member;
            }
        }
        return null;
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
