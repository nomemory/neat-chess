package net.andreinc.neatchess.exception;

public class UCIUnknownCommandException extends RuntimeException {
    public UCIUnknownCommandException(String msg) {
        super(msg);
    }
}
