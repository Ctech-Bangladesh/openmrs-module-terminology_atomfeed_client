package org.bahmni.module.terminology.infrastructure.config;


import org.apache.log4j.Logger;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.Properties;

import static org.apache.commons.lang.ArrayUtils.subarray;
import static org.apache.commons.lang.StringUtils.join;

@Component
public class TRFeedProperties extends AtomFeedProperties {

    private static final String PATH_TO_PROPERTIES = "/.OpenMRS/tr_atomfeed_properties.properties";

    @Resource(name = "terminologyFeedProperties")
    private Properties defaultProperties;

    public static final String TERMINOLOGY_FEED_URI = "terminology.feed.url";

    public static final String DIAGNOSIS_FEED_URI = "diagnosis.feed.url";
    public static final java.lang.String REFERENCE_FEED_URI = "reference.term.feed.url";
    public static final String CONNECT_TIMEOUT = "feed.connectionTimeoutInMilliseconds";
    public static final String MAX_FAILED_EVENTS = "feed.maxFailedEvents";
    public static final String READ_TIMEOUT = "feed.replyTimeoutInMilliseconds";

    private Properties atomFeedProperties;

    @PostConstruct
    public void init() {
        try {
            Properties feedProperties = new Properties();
            FileInputStream file = new FileInputStream(System.getProperty("user.home") + PATH_TO_PROPERTIES);
            feedProperties.load(file);
            atomFeedProperties = feedProperties;
        } catch (Exception ignored) {
            Logger.getLogger(TRFeedProperties.class).info("Atom feed property file not found in " + System.getProperty("user.home") + PATH_TO_PROPERTIES + " Using defaults");
            atomFeedProperties = defaultProperties;
        }
    }

    public TRFeedProperties() {
    }

    public TRFeedProperties(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
        this.init();
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

    public String getDiagnosisFeedUrl() {
        return atomFeedProperties.getProperty(DIAGNOSIS_FEED_URI);
    }

    public String getReferenceTermFeedUrl() {
        return atomFeedProperties.getProperty(REFERENCE_FEED_URI);
    }

    public String getDiagnosisUrl(String content) {
        return join(subarray(getDiagnosisFeedUrl().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }

    public String getReferenceTermUrl(String content) {
        return join(subarray(getReferenceTermFeedUrl().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }
}
