package net.andreinc.neatchess.client.exception;

public class UCIInterruptedException extends UCIRuntimeException{
    public UCIInterruptedException(Throwable cause) {
        super(cause);
    }

    public UCIInterruptedException(String message) {
        super(message);
    }
}
