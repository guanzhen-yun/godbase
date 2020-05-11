package com.ziroom.net.exception;

/**
 * 异常
 */
public class ServerException extends RuntimeException {
    private int code;
    private int error;
    private String msg;

    public ServerException(int code, int error, String msg) {
        this.code = code;
        this.error = error;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public int getError() {
        return error;
    }


    public void setError(int error) {
        this.error = error;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }
}
