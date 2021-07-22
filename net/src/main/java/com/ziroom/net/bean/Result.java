package com.ziroom.net.bean;

import java.io.Serializable;

/**
 * 结果
 */
public class Result<T> implements Serializable {
    private int error;
    private int code;
    private T data;
    private String message;

    public String getErrmsg() {
        return message;
    }

    public String getErrcode() {
        return "" + code;
    }


    public void setErrcode(int errcode) {
        this.code = errcode;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
