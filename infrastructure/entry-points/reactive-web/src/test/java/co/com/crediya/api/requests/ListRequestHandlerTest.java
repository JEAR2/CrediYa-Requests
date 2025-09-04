package co.com.crediya.api.requests;

import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.model.request.Request;
import co.com.crediya.usecase.request.RequestUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListRequestHandlerTest {
    @Mock
    private RequestUseCase requestUseCase;

    @Mock
    private RequestDTOMapper requestDTOMapper;

    private ListRequestHandler listRequestHandler;

    private Request request;

    @BeforeEach
    void setUp() {
        listRequestHandler = new ListRequestHandler(requestUseCase, requestDTOMapper);

        request = Request.builder()
                .id("1")
                .email("test@test.com")
                .amount(BigDecimal.valueOf(1000))
                .period(12)
                .idLoanType(1L)
                .idState(1L)
                .build();
    }

    @Test
    void listByStates_WhenRequestsFound_ShouldReturnCreatedResponse() {
        // Arrange
        ResponseRequestDTO responseDTO = new ResponseRequestDTO(
                request.getAmount(),
                request.getPeriod(),
                request.getEmail(),
                request.getIdState(),
                request.getIdLoanType(),
                "", "", request.getAmount(), "", BigDecimal.TEN
        );

        when(requestUseCase.findRequestByState(anyList(), eq(0), eq(10)))
                .thenReturn(Flux.just(request));

        when(requestDTOMapper.toResponseDTO(request)).thenReturn(responseDTO);

        ServerRequest serverRequest = MockServerRequest.builder()
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("states", "PENDING,APPROVED")
                .build();

        // Act
        Mono<ServerResponse> responseMono = listRequestHandler.listByStates(serverRequest);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(serverResponse -> {
                    assertEquals(HttpStatus.CREATED, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(requestUseCase).findRequestByState(List.of("PENDING", "APPROVED"), 0, 10);
        verify(requestDTOMapper).toResponseDTO(request);
    }

    @Test
    void listByStates_WhenNoRequestsFound_ShouldReturnCreatedResponseWithEmptyList() {
        // Arrange
        when(requestUseCase.findRequestByState(anyList(), eq(0), eq(10)))
                .thenReturn(Flux.empty());

        ServerRequest serverRequest = MockServerRequest.builder()
                .queryParam("page", "0")
                .queryParam("size", "10")
                .build();

        // Act
        Mono<ServerResponse> responseMono = listRequestHandler.listByStates(serverRequest);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(serverResponse -> {
                    assertEquals(HttpStatus.CREATED, serverResponse.statusCode());
                })
                .verifyComplete();

        verify(requestUseCase).findRequestByState(Collections.emptyList(), 0, 10);
        verifyNoInteractions(requestDTOMapper);
    }
}