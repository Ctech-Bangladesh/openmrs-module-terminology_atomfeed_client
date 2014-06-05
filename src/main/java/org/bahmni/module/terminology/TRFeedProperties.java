package org.bahmni.module.terminology;


import org.bahmni.module.terminology.util.TRUtil;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

import static org.bahmni.module.terminology.util.TRUtil.substringBefore;


@Component
public class TRFeedProperties extends AtomFeedProperties {


    private static final String TERMINOLOGY_FEED_URI = "terminology.feed.url";
    private static final String CONNECT_TIMEOUT = "feed.connectionTimeoutInMilliseconds";
    private static final String MAX_FAILED_EVENTS = "feed.maxFailedEvents";
    private static final String READ_TIMEOUT = "feed.replyTimeoutInMilliseconds";
    private static final String SYSTEM_USERNAME = "openmrs.system.username";

    @Resource(name = "terminologyFeedProperties")
    private Properties atomFeedProperties;

    public TRFeedProperties(){

    }


    public TRFeedProperties(Properties properties){
        this.atomFeedProperties = properties;
    }

    public String terminologyFeedUri() {
        return atomFeedProperties.getProperty(TERMINOLOGY_FEED_URI);
    }

    @Override
    public int getMaxFailedEvents() {
        return Integer.parseInt(atomFeedProperties.getProperty(MAX_FAILED_EVENTS));
    }

    @Override
    public int getReadTimeout() {
        return Integer.parseInt(atomFeedProperties.getProperty(READ_TIMEOUT));
    }

    @Override
    public int getConnectTimeout() {
        return Integer.parseInt(atomFeedProperties.getProperty(CONNECT_TIMEOUT));
    }


    public String terminologyServerPrefix() {
        return substringBefore(terminologyFeedUri(), "/", 3);
    }
}
