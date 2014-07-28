package org.bahmni.module.terminology.application.model;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum ConceptType {

    Diagnosis {
        @Override
        public boolean matches(String conceptClass) {
            return StringUtils.equalsIgnoreCase("diagnosis", conceptClass);
        }
    },

    ChiefComplaint {

        private List<String> matchingClasses = Arrays.asList("finding", "symptom", "symptom/finding");

        @Override
        public boolean matches(String conceptClass) {
            for (String matchingClass : matchingClasses) {
                if (StringUtils.equalsIgnoreCase(conceptClass, matchingClass)) {
                    return true;
                }
            }
            return false;
        }
    };

    public abstract boolean matches(String conceptClass);
}
