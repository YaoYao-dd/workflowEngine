package com.yaod.workflow.engine.core.domain.utils;

import com.github.f4b6a3.ulid.UlidCreator;

/**
 * @author Yaod
 **/
public class Utils {

    public static String newUlId(){
        return UlidCreator.getUlid().toString();
    }
}
