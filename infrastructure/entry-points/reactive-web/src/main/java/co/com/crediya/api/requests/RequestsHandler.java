package co.com.crediya.api.requests;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.usecase.request.RequestUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestsHandler {
    private final RequestUseCase requestUseCase;
    private final RequestDTOMapper requestDTOMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> listenSaveRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateRequestDTO.class)
                .map(requestDTOMapper::toRequest)
                .doOnNext(user -> log.debug("Request recibida: {}", user))
                .flatMap(requestUseCase::saveRequest)
                .doOnSuccess(savedRequest -> log.info("Usuario guardado con id={}", savedRequest.getId()))
                .doOnError(err -> log.error("Error guardando la solicitud: {}", err.getMessage(), err))
                .map(requestDTOMapper::toResponseDTO)
                .flatMap(saved ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(saved)
                ).as(transactionalOperator::transactional);
    }

}
