package org.bahmni.module.terminology.infrastructure.model;


import org.ict4h.atomfeed.client.AtomFeedProperties;

import java.util.Properties;

import static org.apache.commons.lang.ArrayUtils.subarray;
import static org.apache.commons.lang.StringUtils.join;


public class TRFeedProperties extends AtomFeedProperties {

    private static final String TERMINOLOGY_FEED_URI = "terminology.feed.url";
    private static final String CONNECT_TIMEOUT = "feed.connectionTimeoutInMilliseconds";
    private static final String MAX_FAILED_EVENTS = "feed.maxFailedEvents";
    private static final String READ_TIMEOUT = "feed.replyTimeoutInMilliseconds";

    private Properties atomFeedProperties;

    public TRFeedProperties(Properties properties) {
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


    public String getTerminologyUrl(String content) {
        return join(subarray(terminologyFeedUri().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }
}
