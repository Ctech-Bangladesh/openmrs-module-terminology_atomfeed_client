package org.bahmni.module.terminology.application.util;


import com.google.gson.Gson;
import org.openmrs.module.webservices.rest.SimpleObject;

import java.io.IOException;

public class SimpleObjectUtil {

    public static SimpleObject toSimpleObject(Object entity) throws IOException {
        return SimpleObject.parseJson(new Gson().toJson(entity));
    }
}
