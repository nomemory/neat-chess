package net.andreinc.neatchess.client;

import net.andreinc.neatchess.exception.UCIRuntimeException;

public class UCIResponse<T> {

    private T result;
    private Throwable exception;

    public UCIResponse(T result) {
        this.result = result;
    }

    protected UCIResponse() {
    }

    public UCIResponse(T result, Throwable exception) {
        this(result);
        this.exception = exception;
    }

    public T getResult() {
        if (!success()) {
            if (exception instanceof RuntimeException) {
                exception.printStackTrace();
                throw (RuntimeException) exception;
            }
            else {
                exception.printStackTrace();
                throw new UCIRuntimeException(exception);
            }
        }
        return this.result;
    }

    public boolean success() {
        return exception == null;
    }

    public Throwable getException() {
        return this.exception;
    }

    protected void setException(Throwable t) {
        this.exception = t;
    }

    @Override
    public String toString() {
        return "UCIResponse{" +
                "result=" + result +
                ", exception=" + exception +
                '}';
    }
}
