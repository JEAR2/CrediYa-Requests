package co.com.crediya.model.exceptions;


import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;

public class RequestForbiddenException extends RequestException {
    public RequestForbiddenException(String message) {
        super(ExceptionStatusCode.FORBIDDEN, message,403);
    }
}
