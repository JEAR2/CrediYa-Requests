package co.com.crediya.model.exceptions;

import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;

public class RequestBadRequestException extends RequestException {
    public RequestBadRequestException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST_VALIDATE, message,400);
    }
}
