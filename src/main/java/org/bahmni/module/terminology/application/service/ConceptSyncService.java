package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.bahmni.module.terminology.application.model.ConceptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConceptSyncService {

    private SHReferenceTermService shReferenceTermService;
    private SHConceptService shConceptService;

    @Autowired
    public ConceptSyncService(SHReferenceTermService shReferenceTermService, SHConceptService shConceptService) {
        this.shConceptService = shConceptService;
        this.shReferenceTermService = shReferenceTermService;
    }

    public void sync(ConceptRequest conceptRequest, ConceptType conceptType) {
        shReferenceTermService.sync(conceptRequest.getConceptReferenceTermRequests());
        shConceptService.sync(conceptRequest, conceptType);
    }
}
