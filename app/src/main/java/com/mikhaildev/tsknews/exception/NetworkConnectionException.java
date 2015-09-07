package com.mikhaildev.tsknews.exception;

import java.io.IOException;


public class NetworkConnectionException extends IOException {

    public NetworkConnectionException() {
        super();
    }

    public NetworkConnectionException(final String message) {
        super(message);
    }

    public NetworkConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkConnectionException(final Throwable cause) {
        super(cause);
    }
}
