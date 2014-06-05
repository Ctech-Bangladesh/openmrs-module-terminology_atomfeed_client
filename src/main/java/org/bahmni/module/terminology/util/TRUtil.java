package org.bahmni.module.terminology.util;

import static org.apache.commons.lang.ArrayUtils.subarray;
import static org.apache.commons.lang.StringUtils.join;

public class TRUtil {

    public static String substringBefore(String str, String character, int position){
        return join(subarray(str.split("/"), 0, position), character);
    }

}
