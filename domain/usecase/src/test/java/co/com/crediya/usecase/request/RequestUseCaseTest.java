package co.com.crediya.usecase.request;

import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.model.user.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class RequestUseCaseTest {

    private RequestRepository requestRepository;
    private RequestUseCase requestUseCase;
    private UserGateway userGateway;
    private Request createRequest() {
        return new Request().toBuilder().id("1").amount(new BigDecimal("1000")).email("a@a.com").idState(1L).idLoanType(1L).period(4).build();
    }


    @BeforeEach
    void setUp() {
        requestRepository = Mockito.mock(RequestRepository.class);
        requestUseCase = Mockito.mock(RequestUseCase.class);
        userGateway = Mockito.mock(UserGateway.class);
        requestUseCase = new RequestUseCase(requestRepository,userGateway);
    }

    @Test
    void saveRequest_WhenValid_ShouldSave() {
        Request request = createRequest();


        Mockito.when(userGateway.findByEmail(request.getEmail())).thenReturn(Mono.just(true));
        Mockito.when(requestRepository.save(any(Request.class))).thenReturn(Mono.just(request));



        StepVerifier.create(requestUseCase.saveRequest(request))
                .expectNext(request)
                .verifyComplete();

        verify(userGateway, times(1)).findByEmail(request.getEmail());
        verify(requestRepository, times(1)).save(request);
    }
    @Test
    void saveRequest_WhenUserDoesNotExist_ShouldError() {

        Request request = createRequest();
        Mockito.when(userGateway.findByEmail(request.getEmail())).thenReturn(Mono.just(false));


        StepVerifier.create(requestUseCase.saveRequest(request))
                .expectError(RequestResourceNotFoundException.class)
                .verify();

        verify(userGateway, times(1)).findByEmail(request.getEmail());
    }



}