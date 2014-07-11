package org.bahmni.module.terminology.util;


import java.util.Map;

public class CollectionUtil {

    public static <K, V> V safeGet(Map<K, V> map, K key, V defaultValue) {
        if (map == null) {
            return null;
        } else {
            return map.containsKey(key) ? map.get(key) : defaultValue;
        }
    }

    public static <K, V> V safeGet(Map<K, V> map, K key) {
        if (map == null) {
            return null;
        } else {
            return map.containsKey(key) ? map.get(key) : null;
        }
    }

    public static <K> Map safeGetMap(Map<K, Object> map, K key) {
        if (map == null) {
            return null;
        } else {
            return map.containsKey(key) ? (Map) map.get(key) : null;
        }
    }

}
