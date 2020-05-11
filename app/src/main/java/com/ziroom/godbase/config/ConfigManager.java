package com.ziroom.godbase.config;


import com.ziroom.godbase.BuildConfig;

/**
 * 域名配置,在application中初始化
 */
public class ConfigManager {
    private static volatile ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * host域名zhugefang
     */

    public String getHost() {
        return BuildConfig.DOMAIN_NAME;
    }
}
