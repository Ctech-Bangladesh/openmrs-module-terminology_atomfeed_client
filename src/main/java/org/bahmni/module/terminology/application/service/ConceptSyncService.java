package org.bahmni.module.terminology.application.service;

import org.bahmni.module.terminology.application.model.ConceptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ConceptSyncService {

    @Autowired
    private SHReferenceTermService shReferenceTermService;

    @Autowired
    private SHConceptService shConceptService;

    public ConceptSyncService() {
    }
    public ConceptSyncService(SHReferenceTermService shReferenceTermService, SHConceptService shConceptService) {
        this.shConceptService = shConceptService;
        this.shReferenceTermService = shReferenceTermService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void sync(ConceptRequest conceptRequest) {
        shReferenceTermService.sync(conceptRequest.getConceptReferenceTermRequests());
        shConceptService.sync(conceptRequest);
    }
}
