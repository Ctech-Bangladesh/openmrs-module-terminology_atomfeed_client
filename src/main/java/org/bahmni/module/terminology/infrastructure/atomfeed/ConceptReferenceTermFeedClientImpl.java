package org.bahmni.module.terminology.infrastructure.atomfeed;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


@Component("conceptReferenceTermFeedClient")
public class ConceptReferenceTermFeedClientImpl implements ConceptReferenceTermFeedClient {

    private final Logger logger = Logger.getLogger(ConceptReferenceTermFeedClientImpl.class);

    @Override
    public void sync() throws URISyntaxException {
        logger.info("Sync Start: Diagnosis Terminology Concepts ..... ");
    }
}
