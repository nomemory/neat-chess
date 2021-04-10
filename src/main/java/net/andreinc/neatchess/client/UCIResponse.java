package net.andreinc.neatchess.client;

public class UCIResponse<T> {

    private T result;
    private RuntimeException exception;

    public UCIResponse(T result) {
        this.result = result;
    }

    protected UCIResponse() {
    }

    public UCIResponse(T result, RuntimeException exception) {
        this(result);
        this.exception = exception;
    }

    public T getResult() {
        return this.result;
    }

    public T getResultOrThrow() {
        if (!success()) {
            throw exception;
        }
        return this.result;
    }

    public boolean success() {
        return exception == null;
    }

    public Throwable getException() {
        return this.exception;
    }

    protected void setException(RuntimeException t) {
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
