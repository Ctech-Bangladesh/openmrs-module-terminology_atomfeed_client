package org.bahmni.module.terminology.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class CollectionUtils {

    public static <K, V> V safeGet(Map<K, V> map, K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public static <K> Map safeGetMap(Map<K, Object> map, K key) {
        return map.containsKey(key) ? (Map) map.get(key) : null;
    }

    public static <T> T first(Collection<T> xs) {
        ArrayList<T> list = new ArrayList<T>();
        list.addAll(xs);
        return first(list);
    }

    public static <T> T first(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

}
