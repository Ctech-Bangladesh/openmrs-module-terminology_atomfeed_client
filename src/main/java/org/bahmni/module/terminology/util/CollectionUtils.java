package org.bahmni.module.terminology.util;


import java.util.Map;

public class CollectionUtils {

    public static <K, V> V safeGet(Map<K, V> map, K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

}
