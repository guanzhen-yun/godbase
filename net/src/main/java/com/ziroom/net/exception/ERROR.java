package com.ziroom.net.exception;

/**
 * 错误
 */
public class ERROR {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;

    /**
     * 无网络
     */
    public static final int NO_NET_ERROR = 1004;
    /**
     * 请求超时
     */
    public static final int TIME_OUT_ERROR = 1005;
    /**
     * 请求取消
     */
    public static final int REQUEST_INTERRUPTED_ERROR = 1006;
    /**
     * TOKEN异常
     */
    public static final int REQUEST_TOKEN_ERROR = 1008;

}
