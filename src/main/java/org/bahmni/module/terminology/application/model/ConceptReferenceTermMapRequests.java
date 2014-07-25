package org.bahmni.module.terminology.application.model;

import org.apache.commons.lang.StringUtils;
import org.openmrs.ConceptReferenceTermMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConceptReferenceTermMapRequests {

    private Set<ConceptReferenceTermMapRequest> referenceTermMapRequests = new HashSet<>();

    public void add(ConceptReferenceTermMapRequest request) {
        referenceTermMapRequests.add(request);
    }

    public Set<ConceptReferenceTermMapRequest> selectNewMappings(Collection<ConceptReferenceTermMap> existingMaps) {
        Set<ConceptReferenceTermMapRequest> result = new HashSet<>();
        for (ConceptReferenceTermMapRequest mapRequest : referenceTermMapRequests) {
            if (!find(mapRequest, existingMaps)) {
                result.add(mapRequest);
            }
        }
        return result;
    }

    public Set<ConceptReferenceTermMapRequest> selectExistingMappings(Collection<ConceptReferenceTermMap> existingMaps) {
        Set<ConceptReferenceTermMapRequest> result = new HashSet<>();
        for (ConceptReferenceTermMapRequest mapRequest : referenceTermMapRequests) {
            if (find(mapRequest, existingMaps)) {
                result.add(mapRequest);
            }
        }
        return result;
    }

    public boolean findMapping(ConceptReferenceTermMap map) {
        for (ConceptReferenceTermMapRequest referenceTermMapRequest : referenceTermMapRequests) {
            if (StringUtils.equals(referenceTermMapRequest.getUuid(), map.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private boolean find(ConceptReferenceTermMapRequest request, Collection<ConceptReferenceTermMap> existingMaps) {
        for (ConceptReferenceTermMap referenceTermMap : existingMaps) {
            if (StringUtils.equals(referenceTermMap.getUuid(), request.getUuid())) {
                return true;
            }
        }
        return false;
    }
}
