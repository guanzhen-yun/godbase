package com.ziroom.net.config;

import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 公共参数interceptor
 */
public class ParamsInterceptor implements Interceptor {
    private Map<String, String> mHeaders;
    private Map<String, String> mCommonParams;

    public ParamsInterceptor(Map<String, String> headers, Map<String, String> commonParams) {
        mHeaders = headers;
        mCommonParams = commonParams;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = handlerRequest(request);
        if (request == null) {
            throw new RuntimeException("Request返回值不能为空");
        }
        return chain.proceed(request);
    }

    /**
     * 添加公共参数
     */
    private Request handlerRequest(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        Headers.Builder headerBuilder = request.headers().newBuilder();
        Map<String, String> params = parseParams(request);
        String method = request.method();
        if (params == null) {
            params = new HashMap<>();
        }
        Map<String, String> addParams = new HashMap<>();
        //公共Header--目前只有一个token
        if(mHeaders != null) {
            for (String key : mHeaders.keySet()) {
                headerBuilder.add(key, mHeaders.get(key));
            }
        }

        requestBuilder.headers(headerBuilder.build());
        //公共参数
        if(mCommonParams != null) {
            for (String key : mCommonParams.keySet()) {
                addParams.put(key, mCommonParams.get(key));
            }
        }
        //防止以前业务逻辑传递city不一样。
        if ("GET".equals(method)) {
            return injectParamsIntoUrl(request.url().newBuilder(), request.newBuilder(), addParams);
        }
        if (canInjectIntoBody(request)) {
            String postBodyString = bodyToString(params) + bodyToString(addParams);
            requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString.substring(0, postBodyString.length() - 1)));
        }
        request = requestBuilder.build();
        return request;
    }

    /**
     * 解析请求参数
     */
    private static Map<String, String> parseParams(Request request) {
        String method = request.method();
        Map<String, String> params = null;
        if ("GET".equals(method)) {
            params = doGet(request);
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            RequestBody body = request.body();
            if (body instanceof FormBody) {
                params = doForm(request);
            }
        }
        return params;
    }

    /**
     * 获取get方式的请求参数
     */
    private static Map<String, String> doGet(Request request) {
        Map<String, String> params = null;
        HttpUrl url = request.url();
        Set<String> strings = url.queryParameterNames();
        if (strings != null) {
            Iterator<String> iterator = strings.iterator();
            params = new HashMap<>();
            int i = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();
                String value = url.queryParameterValue(i);
                params.put(name, value);
                i++;
            }
        }
        return params;
    }

    /**
     * 获取表单的请求参数
     */
    private static Map<String, String> doForm(Request request) {
        Map<String, String> params = null;
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    if (!TextUtils.isEmpty(body.value(i))) {
                        params.put(body.name(i), body.value(i));
                    }
                }
            }
        }
        return params;
    }

    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (!paramsMap.isEmpty()) {
            for (Object o : paramsMap.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                if (TextUtils.isEmpty((String) entry.getValue())) {
                    continue;
                }
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }
        return null;
    }

    /**
     * 确认是否是 post 请求
     *
     * @param request 发出的请求
     * @return true 需要注入公共参数
     */
    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        return TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded");
    }

    /**
     * 转换请求的postbody串
     */

    private static String bodyToString(Map<String, String> request) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : request.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.toString();
    }
}
