package co.com.crediya.api.exceptions;

import co.com.crediya.api.exceptions.model.CustomError;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalHandlerException extends AbstractErrorWebExceptionHandler {


    public GlobalHandlerException(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(serverCodecConfigurer.getReaders());
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request){

        Map<String, Object> errorMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int statusCode = errorMap.get("error") instanceof CustomError customError ? customError.getStatusCode() : (HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ServerResponse.status(statusCode).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorMap));

    }


}
