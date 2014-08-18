package org.bahmni.module.terminology.infrastructure.mapper;

import org.apache.commons.lang.StringUtils;
import org.bahmni.module.terminology.application.model.ConceptMappings;
import org.bahmni.module.terminology.application.model.ConceptNames;
import org.openmrs.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Component
public class ConceptUpdate {

    public Concept merge(Concept newConcept, Concept existingConcept) {
        existingConcept.setRetired(newConcept.isRetired());
        existingConcept.setRetireReason(newConcept.getRetireReason());
        existingConcept.setSet(newConcept.isSet());
        existingConcept.setVersion(newConcept.getVersion());
        mapConceptDataType(newConcept, existingConcept);
        existingConcept.setConceptClass(newConcept.getConceptClass());
        mergeFullySpecifiedName(existingConcept, newConcept);
        mergeConceptNames(existingConcept, new ConceptNames(newConcept.getNames()));
        mergeConceptMappings(existingConcept, new ConceptMappings(newConcept.getConceptMappings()));
        mergeConceptDescriptions(existingConcept, newConcept);
        mergeConceptSets(existingConcept, newConcept);
        mergeConceptAnswers(existingConcept, newConcept);
        return existingConcept;
    }

    public Concept mergeSpecifics(Concept savedConcept, Concept newConcept) {
        if (savedConcept instanceof ConceptNumeric && newConcept instanceof ConceptNumeric) {
            ((ConceptNumeric) savedConcept).setHiAbsolute(((ConceptNumeric) newConcept).getHiAbsolute());
            ((ConceptNumeric) savedConcept).setHiCritical(((ConceptNumeric) newConcept).getHiCritical());
            ((ConceptNumeric) savedConcept).setHiNormal(((ConceptNumeric) newConcept).getHiNormal());
            ((ConceptNumeric) savedConcept).setLowNormal(((ConceptNumeric) newConcept).getLowNormal());
            ((ConceptNumeric) savedConcept).setLowCritical(((ConceptNumeric) newConcept).getLowCritical());
            ((ConceptNumeric) savedConcept).setLowAbsolute(((ConceptNumeric) newConcept).getLowAbsolute());
            ((ConceptNumeric) savedConcept).setUnits(((ConceptNumeric) newConcept).getUnits());
            ((ConceptNumeric) savedConcept).setPrecise(((ConceptNumeric) newConcept).getPrecise());
        }
        return savedConcept;
    }

    private void mergeConceptAnswers(Concept existingConcept, Concept newConcept) {
        if (isNotEmpty(newConcept.getAnswers())) {
            for (ConceptAnswer conceptAnswer : new ArrayList<>(newConcept.getAnswers())) {
                if (!foundAnswer(existingConcept, conceptAnswer)) {
                    existingConcept.addAnswer(conceptAnswer);
                }
            }
        } else if (isNotEmpty(existingConcept.getAnswers())) {
            for (ConceptAnswer conceptAnswer : new ArrayList<>(existingConcept.getAnswers())) {
                if (!foundAnswer(newConcept, conceptAnswer)) {
                    existingConcept.removeAnswer(conceptAnswer);
                }
            }
        }
    }

    private boolean foundAnswer(Concept newConcept, ConceptAnswer conceptAnswer) {
        if (null != newConcept.getAnswers()) {
            for (ConceptAnswer answer : newConcept.getAnswers()) {
                if (StringUtils.equals(answer.getAnswerConcept().getUuid(), conceptAnswer.getAnswerConcept().getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void mapConceptDataType(Concept newConcept, Concept concept) {
        concept.setDatatype(newConcept.getDatatype());
    }

    private void mergeConceptSets(Concept existingConcept, Concept newConcept) {
        if (isNotEmpty(newConcept.getSetMembers())) {
            for (Concept concept : newConcept.getSetMembers()) {
                Concept setMember = findSetMember(existingConcept, concept);
                if (null == setMember) {
                    existingConcept.addSetMember(concept);
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
