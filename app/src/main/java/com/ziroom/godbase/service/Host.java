package com.ziroom.godbase.service;

import android.text.TextUtils;

import com.ziroom.godbase.BuildConfig;
import com.ziroom.godbase.util.SharedUtils;

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

  //测试环境
  public static final String commonHostDev = "http://172.23.32.83:8080/";
  //正式环境
  public static final String commonHostRelease = "http://api.abc.service/";

  public static final String DEV = "dev";//测试环境
  public static final String RELEASE = "release";//正式环境

  public static String getCommonHost() {
    String hostEnv = SharedUtils.getInstance().getString(SharedUtils.ENV);
    return TextUtils.isEmpty(hostEnv) ? BuildConfig.DOMAIN_NAME : hostEnv;
  }

  public static String getOtherHost() {
    String otherHostDev = SharedUtils.getInstance().getString(SharedUtils.ENV_OTHER);
    return TextUtils.isEmpty(otherHostDev) ? BuildConfig.DOMAIN_NAME_OTHER : otherHostDev;
  }

  private static final Map<String, String> mHosts = new HashMap<>();

  public static Map<String, String> getHosts() {
    mHosts.put(commonHostKey, getCommonHost());
    mHosts.put(otherHostKey, getOtherHost());
    return mHosts;
  }
}
