package net.andreinc.neatchess.exception;

public class UCIRuntimeException extends RuntimeException {
    public UCIRuntimeException(Throwable cause) {
        super(cause);
    }

    public UCIRuntimeException(String message) {
        super(message);
    }
}
