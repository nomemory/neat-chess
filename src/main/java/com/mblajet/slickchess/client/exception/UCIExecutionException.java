package com.mblajet.slickchess.client.exception;

public class UCIExecutionException extends UCIRuntimeException {

    public UCIExecutionException(Throwable cause) {
        super(cause);
    }

    public UCIExecutionException(String message) {
        super(message);
    }
}
