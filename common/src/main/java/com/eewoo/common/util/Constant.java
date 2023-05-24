package com.eewoo.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 一些常见的返回结果代码
 */
public class Constant {
    public static final String success = "0";
    public static final String fail = "-1";

    public static final Map<String,String> roleTableMap = new HashMap<String,String>(){{
        put("s","user_supervisor");
        put("c","user_counselor");
        put("a","user_admin");
        put("v","user_visitor");
    }};

}
