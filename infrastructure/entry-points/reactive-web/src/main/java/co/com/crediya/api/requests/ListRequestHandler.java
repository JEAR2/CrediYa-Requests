package co.com.crediya.api.requests;

import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestPaginationDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.usecase.loantype.LoanTypeUseCasePort;
import co.com.crediya.usecase.request.RequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListRequestHandler {

    private final RequestUseCase requestUseCase;
    private final RequestDTOMapper requestDTOMapper;

    public Mono<ServerResponse> listarPorEstados(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        List<Long> states = request.queryParam("states")
                .map(s -> Arrays.stream(s.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return requestUseCase.findRequestByState(states, page, size)
                .map(requestDTOMapper::toResponseDTO)
                .collectList()
                .flatMap(content -> {
                    ResponseRequestPaginationDTO<ResponseRequestDTO> response = new ResponseRequestPaginationDTO<>(page, size, content);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }
}
