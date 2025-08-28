package co.com.crediya.model.exceptions.enums;

public enum ExceptionStatusCode {
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int statusCode;

    private ExceptionStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
