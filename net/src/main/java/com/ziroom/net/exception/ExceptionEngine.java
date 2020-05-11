package com.ziroom.net.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * 异常
 */
public class ExceptionEngine {

    public static final String STR_HTTP_ERROR = "服务器连接失败，请稍后重试";
    public static final String STR_PARSE_ERROR = "服务器连接异常，请稍后重试";
    public static final String STR_NO_NET_ERROR = "无网络连接，请检查您的网络";
    public static final String STR_REQUEST_INTERRUPTED_ERROR = "服务器连接中断，请稍后重试";
    public static final String STR_TIME_OUT_ERROR = "服务器连接超时，请稍后重试";
    public static final String STR_REQUEST_TOKEN_ERROR = "请登录";
    public static final String STR_UNKNOWN = "未知错误";
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException || e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            ex.setDisplayMessage(STR_HTTP_ERROR);
            return ex;
        }
        if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setDisplayMessage(resultException.getMsg());
            return ex;
        }
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.setDisplayMessage(STR_PARSE_ERROR);
            return ex;
            //环信无网络判断
        }
        if (e instanceof SocketException ||
                e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR.NO_NET_ERROR);
            ex.setDisplayMessage(STR_NO_NET_ERROR);
            return ex;
        }
        if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ERROR.TIME_OUT_ERROR);
            ex.setDisplayMessage(STR_TIME_OUT_ERROR);
            return ex;
        }
        if (e instanceof InterruptedException) {
            ex = new ApiException(e, ERROR.REQUEST_INTERRUPTED_ERROR);
            ex.setDisplayMessage(STR_REQUEST_INTERRUPTED_ERROR);
            return ex;
        }
        if (e instanceof ApiException) {

            return (ApiException) e;
        }
        ex = new ApiException(e, ERROR.UNKNOWN);
        ex.setDisplayMessage(STR_UNKNOWN);
        return ex;
    }
}
