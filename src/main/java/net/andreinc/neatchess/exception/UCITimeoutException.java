package net.andreinc.neatchess.exception;

public class UCITimeoutException extends RuntimeException {
    public UCITimeoutException(Throwable cause) {
        super(cause);
    }
}
