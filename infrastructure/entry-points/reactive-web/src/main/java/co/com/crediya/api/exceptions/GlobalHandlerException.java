package co.com.crediya.api.exceptions;

import co.com.crediya.api.exceptions.model.ResponseDTO;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.model.exceptions.RequestException;
import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalHandlerException implements ErrorWebExceptionHandler {


    private final ObjectMapper objectMapper;

    private Mono<Void> buildFailureResponse(ServerWebExchange exchange, HttpStatus status, ResponseDTO<?> error) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(error))
                .map(bytes -> exchange.getResponse().bufferFactory().wrap(bytes))
                .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)))
                .onErrorResume(e -> {
                    log.error("Internal ServerError", e);
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(RequestException.class, authEx -> {
                    log.warn("Authentication Exception: {}", authEx.getMessage());
                    return buildFailureResponse(
                            exchange,
                            HttpStatus.resolve(authEx.getStatus()),
                            HandlersResponseUtil.buildBodyFailureResponse(
                                    authEx.getStatusCode().status(),
                                    authEx.getMessage(),
                                    null
                            )
                    );
                })
                .onErrorResume(ConstraintViolationException.class, validationEx -> {
                    log.warn("Fields invalid Exception: {}", validationEx.getMessage());
                    return toListErrors(validationEx.getConstraintViolations())
                            .flatMap(fieldErrors -> buildFailureResponse(
                                    exchange,
                                    HttpStatus.BAD_REQUEST,
                                    HandlersResponseUtil.buildBodyFailureResponse(
                                            ExceptionStatusCode.FIELDS_BAD_REQUEST.status(),
                                            "Request invalid fields",
                                            fieldErrors
                                    )
                            ));
                }).then()
                .onErrorResume(e -> {
                    log.error("Internal Server Error", e);
                    return buildFailureResponse(
                            exchange,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            HandlersResponseUtil.buildBodyFailureResponse(
                                    ExceptionStatusCode.INTERNAL_SERVER_ERROR.status(),
                                    "Internal Server Error",
                                    null
                            )
                    );
                });
    }

    private Mono<List<String>> toListErrors(Set<ConstraintViolation<?>> violations) {
        return Flux.fromIterable(violations)
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collectList();
    }

}
