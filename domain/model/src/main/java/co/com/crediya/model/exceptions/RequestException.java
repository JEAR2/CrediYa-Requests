package co.com.crediya.model.exceptions;

import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;

public class RequestException extends RuntimeException{
    private ExceptionStatusCode statusCode;

    public RequestException(ExceptionStatusCode statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }

    public ExceptionStatusCode getStatusCode() {
        return statusCode;
    }
}
