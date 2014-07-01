package org.bahmni.module.terminology.application.mappers;


import java.util.Map;

public interface Mapper <T> {
    T map(Map<String, Object> data);
}
