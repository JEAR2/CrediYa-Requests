package co.com.crediya.model.exceptions.enums;

public enum ExceptionStatusCode {
    BAD_REQUEST("400-BD"),
    BAD_REQUEST_VALIDATE("400-VD"),
    FIELDS_BAD_REQUEST("400-BD-FIELDS"),
    NOT_FOUND("404-NF"),
    CREATED("201-CR"),
    INTERNAL_SERVER_ERROR("500-ISE"),
    OK("200-OK");

    private final String statusCode;

    ExceptionStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode;
    }
}
