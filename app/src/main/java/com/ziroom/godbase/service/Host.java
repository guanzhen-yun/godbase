package com.ziroom.godbase.service;

import com.ziroom.godbase.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 域名映射
 */
public class Host {
    private static final String commonHostKey = "def";
    private static final String otherHostKey = "other";

    public static final String commonHostDomain = "Domain-Name: " + commonHostKey;

    public static final String commonHostOther = "Domain-Name: " + otherHostKey;

    public static String getCommonHost() {
        return BuildConfig.DOMAIN_NAME;
    }

    public static String getOtherHost() {
        return BuildConfig.DOMAIN_NAME_OTHER;
    }

    private static final Map<String, String> mHosts = new HashMap<>();
    public static Map<String, String> getHosts() {
        mHosts.put(commonHostKey, getCommonHost());
        mHosts.put(otherHostKey, getOtherHost());
        return mHosts;
    }
}
