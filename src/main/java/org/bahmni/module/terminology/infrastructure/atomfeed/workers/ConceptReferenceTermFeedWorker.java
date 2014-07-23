package org.bahmni.module.terminology.infrastructure.atomfeed.workers;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.application.service.SHReferenceTermService;
import org.bahmni.module.terminology.infrastructure.config.TRFeedProperties;
import org.bahmni.module.terminology.infrastructure.http.AuthenticatedHttpClient;
import org.bahmni.module.terminology.infrastructure.mapper.ConceptReferenceTermRequestMapper;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;


public class ConceptReferenceTermFeedWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(ConceptReferenceTermFeedWorker.class);

    private ConceptReferenceTermRequestMapper referenceTermRequestMapper;
    private SHReferenceTermService shReferenceTermService;
    private TRFeedProperties properties;
    private AuthenticatedHttpClient httpClient;

    public ConceptReferenceTermFeedWorker(AuthenticatedHttpClient httpClient, TRFeedProperties properties, SHReferenceTermService shReferenceTermService, ConceptReferenceTermRequestMapper referenceTermRequestMapper) {
        this.httpClient = httpClient;
        this.properties = properties;
        this.shReferenceTermService = shReferenceTermService;
        this.referenceTermRequestMapper = referenceTermRequestMapper;
    }

    @Override
    public void process(final Event event) {
        logger.info(format("Received concept reference term sync event for %s with conent %s ", event.getFeedUri(), event.getContent()));
        Map conceptReferenceTermMap = httpClient.get(properties.getReferenceTermUrl(event.getContent()), HashMap.class);
        shReferenceTermService.syncReferenceTerm(referenceTermRequestMapper.mapReferenceTerm(conceptReferenceTermMap));
    }

    @Override
    public void cleanUp(Event event) {

    }
}
