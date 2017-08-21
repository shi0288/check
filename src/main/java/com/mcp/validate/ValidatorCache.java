package com.mcp.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiqm on 2017-08-18.
 */
public class ValidatorCache {

    private final static List<BaseValidator> list = new ArrayList<BaseValidator>();

    public static void add(BaseValidator baseValidator) {
        list.add(baseValidator);
    }

    public static List get() {
        return list;
    }

    public static BaseValidator get(int i) {
        return list.get(i);
    }


}
