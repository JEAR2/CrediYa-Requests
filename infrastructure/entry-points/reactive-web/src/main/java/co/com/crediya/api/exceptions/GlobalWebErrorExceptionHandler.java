package co.com.crediya.api.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
@Component
@Order(-2)
public class GlobalWebErrorExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = "500";
        if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            code = "400";
        } else if (ex instanceof IllegalStateException) {
            status = HttpStatus.CONFLICT;
            code = "409";
        }
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Error error = new Error(code,ex.getMessage(),exchange.getRequest().getPath().value());
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(error);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
