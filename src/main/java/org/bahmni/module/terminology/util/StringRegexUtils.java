package org.bahmni.module.terminology.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringRegexUtils {
    //TODO: Making it more generic
    private static final String baseUrlRegex= "^(?:http|https)://.*:([0-9]{1,})";
    private static final int MAX_PORT_DIGIT= 6;

    public String getBaseUrl(String fullUrl){
        Pattern pattern = Pattern.compile(baseUrlRegex);
        Matcher matcher = pattern.matcher(fullUrl);
        String urlPrefix= null;
        if(matcher.find()){
            String portNo= matcher.group(1);
            if(portNo!= null && (!portNo.isEmpty()) && portNo.length()<= MAX_PORT_DIGIT)
                urlPrefix= matcher.group(0);
        }
        return urlPrefix;
    }
}
