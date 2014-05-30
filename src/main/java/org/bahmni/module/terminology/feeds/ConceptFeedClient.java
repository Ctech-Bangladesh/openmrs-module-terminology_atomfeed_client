package org.bahmni.module.terminology.feeds;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;



@Component("conceptFeedClient")
public class ConceptFeedClient {

    public void syncConcepts() {
        Logger logger = Logger.getLogger(ConceptFeedClient.class);
        logger.info("Terminology atom feed started!");
    }
}
