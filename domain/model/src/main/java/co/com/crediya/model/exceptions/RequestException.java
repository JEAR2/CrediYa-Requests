package co.com.crediya.model.exceptions;

import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;
import lombok.Getter;

@Getter
public class RequestException extends RuntimeException{
    private final ExceptionStatusCode statusCode;
    private final int status;
    public RequestException(ExceptionStatusCode statusCode, String message, int status){
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}
