package org.bahmni.module.terminology.application.mapper;

import org.bahmni.module.terminology.application.dtos.ConceptNameDTO;
import org.bahmni.module.terminology.util.Lambda;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.bahmni.module.terminology.util.IfNotNull.ifNotNull;

@Component
public class ConceptNameMapper {

    public ConceptNameDTO map(final Map nameObject) {
        final ConceptNameDTO result = new ConceptNameDTO();
        ifNotNull(nameObject.get("uuid")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setUuid(arg.toString());
            }
        });
        ifNotNull(nameObject.get("display")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setDisplay(arg.toString());
            }
        });
        ifNotNull(nameObject.get("name")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setName(arg.toString());
            }
        });
        ifNotNull(nameObject.get("locale")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setLocale(arg.toString());
            }
        });
        ifNotNull(nameObject.get("localePreferred")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setLocalePreferred(arg.toString());
            }
        });
        ifNotNull(nameObject.get("conceptNameType")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setConceptNameType(arg.toString());
            }
        });
        ifNotNull(nameObject.get("resourceVersion")).apply(new Lambda<Object>() {
            @Override
            public void apply(Object arg) {
                result.setResourceVersion(arg.toString());
            }
        });
        return result;
    }
}
