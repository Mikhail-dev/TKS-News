package com.mikhaildev.tsknews.exception;

import java.io.IOException;


public class ApiException extends IOException {
    private int messageResourceId;
    private Integer responseCode;

    public ApiException(Throwable cause, int resourceStringMessage, Integer _responseCode) {
        super(cause.getMessage(), cause);
        messageResourceId = resourceStringMessage;
        responseCode = _responseCode;
    }

    public ApiException(Throwable cause, int resourceStringMessage) {
        super(cause.getMessage(), cause);
        messageResourceId = resourceStringMessage;
    }

    public ApiException(int resourceStringMessage) {
        super();
        messageResourceId = resourceStringMessage;
    }

    public int getMessageResourceId() {
        return messageResourceId;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
}

