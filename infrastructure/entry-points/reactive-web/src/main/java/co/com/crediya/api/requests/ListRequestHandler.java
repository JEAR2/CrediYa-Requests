package co.com.crediya.api.requests;

import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.usecase.request.RequestUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListRequestHandler {

    private final RequestUseCase requestUseCase;
    private final RequestDTOMapper requestDTOMapper;

    public Mono<ServerResponse> listByStates(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        List<String> states = Optional.ofNullable(request.queryParams().get("states"))
                .map(list -> list.stream()
                        .flatMap(s -> Arrays.stream(s.split(",")))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
                )
                .orElse(Collections.emptyList());

        return requestUseCase.findRequestByState(states, page, size)
                .map(requestDTOMapper::toResponseDTO)
                .collectList()
                .doOnSuccess(results -> log.info("Found {} requests for states={}", results.size(), states))
                .doOnError(ex -> log.error("Error listing requests by states={} page={} size={} - reason={}",
                        states, page, size, ex.getMessage(), ex))
                .flatMap(savedRequest ->
                        ServerResponse.created(URI.create("/requests/state"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(
                                        ExceptionStatusCode.CREATED.status(),
                                        savedRequest
                                ))
                );
    }
}
