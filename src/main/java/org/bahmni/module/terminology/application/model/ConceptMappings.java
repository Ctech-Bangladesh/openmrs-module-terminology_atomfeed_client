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
            return new ArrayList<>(conceptMaps).iterator();
        } else {
            return new ArrayList<ConceptMap>().iterator();
        }
    }

    public ConceptMap findConceptMap(ConceptMap conceptMap) {
        if (conceptMaps == null) {
            return null;
        }
        for (ConceptMap map : conceptMaps) {
            if (hasSameUuid(conceptMap, map)) {
                return map;
            }
        }
        return null;
    }

    private boolean hasSameUuid(ConceptMap conceptMap, ConceptMap map) {
        return StringUtils.equals(map.getConceptReferenceTerm().getUuid(), conceptMap.getConceptReferenceTerm().getUuid());
    }
}
