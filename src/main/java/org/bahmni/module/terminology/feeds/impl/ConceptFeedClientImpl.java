package org.bahmni.module.terminology.feeds.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.feeds.ConceptFeedClient;
import org.springframework.stereotype.Component;



@Component("conceptFeedClient")
public class ConceptFeedClientImpl implements ConceptFeedClient {

    @Override
    public void syncConcepts() {
        Logger logger = Logger.getLogger(ConceptFeedClientImpl.class);
        logger.info("Terminology atom feed started!");
    }
}
