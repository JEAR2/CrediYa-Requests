package co.com.crediya.usecase.request;

import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.notification.QueuePort;
import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.model.state.State;
import co.com.crediya.model.state.gateways.StateRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RequestUseCaseTest {

    private RequestRepository requestRepository;
    private StateRepository stateRepository;
    private LoanTypeRepository loanTypeRepository;
    private RequestUseCase requestUseCase;
    private QueuePort  queuePort;
    private UserGateway userGateway;
    private Request createRequest() {
        return new Request().toBuilder().id(1L).amount(10000.0).email("a@a.com").idState(1L).idLoanType(1L).period(4).build();
    }

    private User createUser() {
        return new User().toBuilder().name("John").lastName("Acevedo").identityDocument("222").birthDate(new Date()).phoneNumber("222").baseSalary(100000.0).build();
    }

    private Request request;
    private State state;
    private LoanType loanType;


    @BeforeEach
    void setUp() {
        requestRepository = Mockito.mock(RequestRepository.class);
        queuePort = Mockito.mock(QueuePort.class);
        requestUseCase = Mockito.mock(RequestUseCase.class);
        stateRepository = Mockito.mock(StateRepository.class);
        loanTypeRepository = Mockito.mock(LoanTypeRepository.class);
        userGateway = Mockito.mock(UserGateway.class);
        requestUseCase = new RequestUseCase(requestRepository,queuePort,stateRepository,loanTypeRepository,userGateway);
        request = Request.builder()
                .id(1L)
                .email("test@test.com")
                .idState(1L)
                .idLoanType(100L)
                .amount(10000.0)
                .basePayment(1000000.0)
                .period(10)
                .interestRate(15.0)
                .build();

        state = State.builder()
                .id(1L)
                .name("PENDING")
                .code("P")
                .build();

        loanType = LoanType.builder()
                .id(100L)
                .name("PERSONAL")
                .interestRate(15.0)
                .build();
    }

    @Test
    void saveRequest_WhenValid_ShouldSave() {
        Request request = createRequest();
        User  user = createUser();

        Mockito.when(userGateway.findByEmail(request.getEmail())).thenReturn(Mono.just(user));
        Mockito.when(requestRepository.save(any(Request.class))).thenReturn(Mono.just(request));



        StepVerifier.create(requestUseCase.saveRequest(request,"a@a.com"))
                .expectNext(request)
                .verifyComplete();

        verify(userGateway, times(1)).findByEmail(request.getEmail());
        verify(requestRepository, times(1)).save(request);
    }
    @Test
    void saveRequest_WhenUserDoesNotExist_ShouldError() {
        Request request = createRequest();

        Mockito.when(userGateway.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(requestUseCase.saveRequest(request, "a@.com"))
                .expectError(RequestResourceNotFoundException.class)
                .verify();

        verify(userGateway, times(1)).findByEmail(Mockito.anyString());
    }

    @Test
    void findRequestByState_WhenDataExists_ShouldEnrichAndReturnRequest() {
        // Arrange
        List<String> states = List.of("P");
        List<Long> stateIds = List.of(1L);
        User  user = createUser();
        when(stateRepository.findByCodeIn(states)).thenReturn(Flux.just(state));
        when(requestRepository.findRequestsByState(stateIds, 0, 10)).thenReturn(Flux.just(request));
        when(stateRepository.findById(1L)).thenReturn(Mono.just(state));
        when(loanTypeRepository.findById(100L)).thenReturn(Mono.just(loanType));
        when(requestRepository.findRequestsByState(stateIds, 0, 10)).thenReturn(Flux.just(request));
        when(requestRepository.findRequestsByStateApprovedByUser(request.getEmail(),"APPROVED")).thenReturn(Flux.just(request));
        when(userGateway.findByEmail("test@test.com")).thenReturn(Mono.just(user));


        // Act
        Flux<Request> result = requestUseCase.findRequestByState(states, 0, 10);

        // Assert
        StepVerifier.create(result)
                .assertNext(enrichedRequest -> {
                    assertEquals("PENDING", enrichedRequest.getState());
                    assertEquals("PERSONAL", enrichedRequest.getType());
                    assertEquals(15.0, enrichedRequest.getInterestRate());
                    assertEquals("John", enrichedRequest.getNameClient());
                    assertEquals(100000.0, enrichedRequest.getBasePayment());
                })
                .verifyComplete();

        verify(stateRepository).findByCodeIn(states);
        verify(requestRepository).findRequestsByState(stateIds, 0, 10);
        verify(stateRepository).findById(1L);
        verify(loanTypeRepository).findById(100L);
        verify(userGateway).findByEmail("test@test.com");
    }

    @Test
    void findRequestByState_WhenNoRequests_ShouldReturnEmptyFlux() {
        // Arrange
        List<String> states = List.of("P");

        when(stateRepository.findByCodeIn(states)).thenReturn(Flux.just(state));
        when(requestRepository.findRequestsByState(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        // Act
        Flux<Request> result = requestUseCase.findRequestByState(states, 0, 10);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(requestRepository).findRequestsByState(anyList(), eq(0), eq(10));
    }



}