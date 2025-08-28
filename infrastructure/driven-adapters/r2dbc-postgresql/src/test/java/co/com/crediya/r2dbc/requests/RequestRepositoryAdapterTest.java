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
import org.springframework.http.RequestEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
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
                .amount(259000.0)
                .period(6)
                .email("a@a.com")
                .idState(1L)
                .idLoanType(1L)
                .build();

        requestEntity = RequestsEntity.builder()
                .id("1")
                .amount(259000.0)
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
}