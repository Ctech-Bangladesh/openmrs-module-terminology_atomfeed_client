package org.bahmni.module.terminology.application.model;

import org.apache.commons.lang.StringUtils;
import org.openmrs.ConceptMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ConceptMappings implements Iterable<ConceptMap> {

    private Collection<ConceptMap> conceptMaps;

    public ConceptMappings(Collection<ConceptMap> conceptMaps) {
        this.conceptMaps = conceptMaps;
    }

    @Override
    public Iterator<ConceptMap> iterator() {
        if (conceptMaps != null) {
            return conceptMaps.iterator();
        } else {
            return new ArrayList<ConceptMap>().iterator();
        }
    }

    public ConceptMap findConceptMap(ConceptMap conceptMap) {
        if (conceptMaps == null) {
            return null;
        }
        for (ConceptMap map : conceptMaps) {
            if (hasSameCode(conceptMap, map) && hasSameSource(conceptMap, map)) {
                return map;
            }
        }
        return null;
    }

    private boolean hasSameSource(ConceptMap conceptMap, ConceptMap map) {
        return StringUtils.equals(map.getConceptReferenceTerm().getConceptSource().getName(), conceptMap.getConceptReferenceTerm().getConceptSource().getName());
    }

    private boolean hasSameCode(ConceptMap conceptMap, ConceptMap map) {
        return map.getConceptReferenceTerm().getCode().equals(conceptMap.getConceptReferenceTerm().getCode());
    }
}
