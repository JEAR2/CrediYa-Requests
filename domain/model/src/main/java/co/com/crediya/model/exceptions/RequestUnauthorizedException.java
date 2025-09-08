package co.com.crediya.model.exceptions;


import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;

public class RequestUnauthorizedException extends RequestException {

    public RequestUnauthorizedException(String message) {
        super(ExceptionStatusCode.UNAUTHORIZED, message,401);
    }
}
