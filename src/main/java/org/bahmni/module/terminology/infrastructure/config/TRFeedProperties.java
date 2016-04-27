package org.bahmni.module.terminology.infrastructure.config;


import org.apache.log4j.Logger;
import org.bahmni.module.terminology.util.StringUtil;
import org.ict4h.atomfeed.client.AtomFeedProperties;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.apache.commons.lang.ArrayUtils.subarray;
import static org.apache.commons.lang.StringUtils.join;

@Component
public class TRFeedProperties extends AtomFeedProperties {

    private static final String TR_FEED_PROPERTIES_FILENAME = "tr_atomfeed_properties.properties";

    @Resource(name = "terminologyFeedProperties")
    private Properties defaultProperties;

    public static final String TERMINOLOGY_FEED_URI_KEY = "concept.feed.url";
    public static final String MEDICATION_FEED_URI_KEY = "medication.feed.url";

    public static final String REFERENCE_FEED_URI_KEY = "referenceTerm.feed.url";
    public static final String CONNECT_TIMEOUT_KEY = "feed.connectionTimeoutInMilliseconds";
    public static final String MAX_FAILED_EVENTS_KEY = "feed.maxFailedEvents";
    public static final String READ_TIMEOUT_KEY = "feed.replyTimeoutInMilliseconds";

    public static final String TR_API_USER_NAME_KEY = "tr.apiUserName";
    public static final String TR_API_USER_PASSWORD_KEY = "tr.apiUserPassword";
    public static final String TR_REFERENCE_PATH_KEY = "tr.referenceUrl";

    private Properties atomFeedProperties;

    @PostConstruct
    public void init() {
        try {
            Properties feedProperties = new Properties();
            File propertiesFile = new File(OpenmrsUtil.getApplicationDataDirectory(), TR_FEED_PROPERTIES_FILENAME);
            FileInputStream file = new FileInputStream(propertiesFile);
            feedProperties.load(file);
            atomFeedProperties = feedProperties;
        } catch (Exception ignored) {
            Logger.getLogger(TRFeedProperties.class).info("Atom feed property file not found in " + System.getProperty("user.home") + TR_FEED_PROPERTIES_FILENAME + " Using defaults");
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
        return atomFeedProperties.getProperty(TERMINOLOGY_FEED_URI_KEY);
    }

    public String getTRBaseUrl(){
        return atomFeedProperties.getProperty(TR_REFERENCE_PATH_KEY);
    }

    @Override
    public int getMaxFailedEvents() {
        return Integer.parseInt(atomFeedProperties.getProperty(MAX_FAILED_EVENTS_KEY));
    }

    @Override
    public int getReadTimeout() {
        return Integer.parseInt(atomFeedProperties.getProperty(READ_TIMEOUT_KEY));
    }

    @Override
    public int getConnectTimeout() {
        return Integer.parseInt(atomFeedProperties.getProperty(CONNECT_TIMEOUT_KEY));
    }

    @Override
    public boolean isHandleRedirection() {
        return true;
    }

    public String getTerminologyUrl(String content) {
        return join(subarray(terminologyFeedUri().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }

    public String getReferenceTermFeedUrl() {
        return atomFeedProperties.getProperty(REFERENCE_FEED_URI_KEY);
    }

    public String getReferenceTermUrl(String content) {
        return join(subarray(getReferenceTermFeedUrl().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }

    public String medicationFeedUri() {
        return atomFeedProperties.getProperty(MEDICATION_FEED_URI_KEY);
    }

    public String getMedicationUrl(String content) {
        return join(subarray(medicationFeedUri().split("/"), 0, 3), "/") + ((null == content) ? "" : content);
    }

    public String getTerminologyApiUserName() {
        return atomFeedProperties.getProperty(TR_API_USER_NAME_KEY).trim();
    }

    public String getTerminologyApiUserPassword() {
        return atomFeedProperties.getProperty(TR_API_USER_PASSWORD_KEY).trim();
    }
}
