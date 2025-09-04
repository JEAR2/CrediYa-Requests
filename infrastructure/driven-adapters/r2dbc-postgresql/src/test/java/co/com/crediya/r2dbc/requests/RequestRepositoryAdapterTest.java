package co.com.crediya.r2dbc.requests;

import co.com.crediya.model.request.Request;
import co.com.crediya.r2dbc.entities.RequestsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestRepositoryAdapterTest {
    @InjectMocks
    RequestRepositoryAdapter requestRepositoryAdapter;

    @Mock
    RequestReactiveRepository requestRepository;

    @Mock
    ObjectMapper objectMapper;

    private Request request;

    private RequestsEntity requestEntity;

    @BeforeEach
    void setUp() {
         request = Request.builder()
                .id("1")
                .amount(new BigDecimal("10000"))
                .period(6)
                .email("a@a.com")
                .idState(1L)
                .idLoanType(1L)
                .build();

        requestEntity = RequestsEntity.builder()
                .id("1")
                .amount(new BigDecimal("10000"))
                .period(6)
                .email("a@a.com")
                .idState(1L)
                .idLoanType(1L).build();
    }

    @Test
    @DisplayName("Must request user successfully")
    void saveRequestWhenRequestCorrect_ShouldSavedRequest() {
        // Arrange
        when(requestRepository.save(requestEntity)).thenReturn(Mono.just(requestEntity));
        when(objectMapper.map(requestEntity, Request.class)).thenReturn(request);
        when(objectMapper.map(request, RequestsEntity.class)).thenReturn(requestEntity);
        // Act
        Mono<Request> result = requestRepositoryAdapter.save(request);
        // Assert
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    @DisplayName("Must find requests by state successfully")
    void findRequestsByState_WhenExists_ShouldReturnRequests() {
        // Arrange
        List<Long> states = List.of(1L);
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepository.findByIdStateIn(states, pageable))
                .thenReturn(Flux.just(requestEntity));
        when(objectMapper.map(requestEntity, Request.class))
                .thenReturn(request);

        // Act
        Flux<Request> result = requestRepositoryAdapter.findRequestsByState(states, 0, 10);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(r -> r.getId().equals("1") && r.getEmail().equals("a@a.com"))
                .verifyComplete();

        verify(requestRepository).findByIdStateIn(states, pageable);
        verify(objectMapper).map(requestEntity, Request.class);
    }

    @Test
    @DisplayName("Must return empty Flux when no requests match state")
    void findRequestsByState_WhenEmpty_ShouldReturnEmptyFlux() {
        // Arrange
        List<Long> states = List.of(99L);
        Pageable pageable = PageRequest.of(0, 10);

        when(requestRepository.findByIdStateIn(states, pageable))
                .thenReturn(Flux.empty());

        // Act
        Flux<Request> result = requestRepositoryAdapter.findRequestsByState(states, 0, 10);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(requestRepository).findByIdStateIn(states, pageable);
    }
}