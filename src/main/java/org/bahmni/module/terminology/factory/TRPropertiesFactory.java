package org.bahmni.module.terminology.factory;

import org.apache.log4j.Logger;
import org.bahmni.module.terminology.TRFeedProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.Properties;

@Component
public class TRPropertiesFactory {

    private static final String PATH_TO_PROPERTIES = "/.OpenMRS/tr_atomfeed_properties.properties";

    @Resource(name = "terminologyFeedProperties")
    private Properties atomFeedProperties;
    private Properties overriddenProperties;

    public TRPropertiesFactory() {
        Properties feedProperties = new Properties();
        try {
            FileInputStream file = new FileInputStream(System.getProperty("user.home") + PATH_TO_PROPERTIES);
            feedProperties.load(file);
            overriddenProperties = feedProperties;
        } catch (Exception ignored) {
            Logger.getLogger(TRPropertiesFactory.class).info("Atom feed property file not found in " + System.getProperty("user.home") + PATH_TO_PROPERTIES + " Using defaults");
        }
    }

    public TRFeedProperties build() {
        return new TRFeedProperties((null != overriddenProperties) ? overriddenProperties : atomFeedProperties);
    }
}
