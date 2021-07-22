package com.ziroom.godbase.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求头
 */
public class HeaderParam {
    private static final Map<String, String> mHeaders = new HashMap<>();
    public static Map<String, String> getHeaderParams() {
        String token = "token";
        mHeaders.put("x-auth-token", token);
        return mHeaders;
    }
}
