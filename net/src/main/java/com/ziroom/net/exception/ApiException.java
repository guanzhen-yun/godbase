package com.ziroom.net.exception;

/**
 * 异常
 */
public class ApiException extends Exception {
    private final Throwable throwable;
    private int code;
    private String displayMessage;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.throwable = throwable;
        this.code = code;

    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }
}
