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

    Procedure {
        @Override
        public boolean matches(String conceptClass) {
            return StringUtils.equalsIgnoreCase("Procedure", conceptClass);
        }
    },

    ChiefComplaint {

        private List<String> matchingClasses = Arrays.asList("symptom", "symptom/finding");

        @Override
        public boolean matches(String conceptClass) {
            return matchingClasses.contains(conceptClass.toLowerCase());
        }
    },

    Default {
        @Override
        public boolean matches(String conceptClass) {
            return true;
        }
    };

    public abstract boolean matches(String conceptClass);
}
