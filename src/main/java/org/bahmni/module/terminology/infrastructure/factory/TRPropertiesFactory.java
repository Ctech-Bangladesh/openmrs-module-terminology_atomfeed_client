package org.bahmni.module.terminology.infrastructure.factory;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;

import java.io.FileInputStream;
import java.util.Properties;

public class TRPropertiesFactory {

    private static final String PATH_TO_PROPERTIES = "/.OpenMRS/tr_atomfeed_properties.properties";
    private Properties atomFeedProperties;

    public TRPropertiesFactory(Properties defaults) {
        try {
            Properties feedProperties = new Properties();
            FileInputStream file = new FileInputStream(System.getProperty("user.home") + PATH_TO_PROPERTIES);
            feedProperties.load(file);
            atomFeedProperties = feedProperties;
        } catch (Exception ignored) {
            Logger.getLogger(TRPropertiesFactory.class).info("Atom feed property file not found in " + System.getProperty("user.home") + PATH_TO_PROPERTIES + " Using defaults");
            atomFeedProperties = defaults;
        }
    }

    public TRFeedProperties build() {
        return new TRFeedProperties(atomFeedProperties);
    }
}
