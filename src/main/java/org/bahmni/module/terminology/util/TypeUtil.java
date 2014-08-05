package org.bahmni.module.terminology.util;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class TypeUtil {

    public static String asString(Object object) {
        return (null == object) ? null : object.toString();
    }

    public static Double asDouble(Object object) {
        String stringValue = asString(object);
        return (isNotBlank(stringValue)) ? parseDouble(stringValue) : null;
    }

    public static Boolean asBoolean(Object object) {
        String stringValue = asString(object);
        return (isNotEmpty(stringValue)) ? parseBoolean(stringValue) : null;
    }
}
