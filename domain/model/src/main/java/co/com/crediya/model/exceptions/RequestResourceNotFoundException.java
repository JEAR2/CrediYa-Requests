package co.com.crediya.model.exceptions;

import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;

public class RequestResourceNotFoundException extends RequestException {
    public RequestResourceNotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message,404);
    }
}
