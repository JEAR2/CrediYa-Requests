package co.com.crediya.api;

import co.com.crediya.api.config.PathsConfig;
import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseLoanTypeDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.api.requests.RequestsHandler;
import co.com.crediya.api.requests.RouterRest;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.ports.TransactionManagement;
import co.com.crediya.model.request.Request;
import co.com.crediya.usecase.loantype.LoanTypeUseCasePort;
import co.com.crediya.usecase.request.RequestUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, RequestsHandler.class})
@EnableConfigurationProperties(PathsConfig.class)
@WebFluxTest
class RouterRestTest {

    private static final String REQUESTS_PATH = "/api/v1/requests";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RequestUseCase requestUseCase;

    @MockitoBean
    private LoanTypeUseCasePort loanTypeUseCasePort;

    @MockitoBean
    private RequestDTOMapper  requestDTOMapper;


    @MockitoBean
    private TransactionManagement transactionManagement;

    @Autowired
    private PathsConfig pathsConfig;

    private final CreateRequestDTO  createRequestDTO = new CreateRequestDTO(1500.0,5,"a@a.com",1L,"CODE1");
    private final ResponseRequestDTO responseRequestDTO = new ResponseRequestDTO(1L,1500.0,5,"a@a.com",1L,1L);
    private final Request request = Request.builder().id("1").amount(150.0).period(2).email("a@a.com").idState(1L).idLoanType(1L).build();
    private final LoanType loanType = LoanType.builder().id(1L).name("Tipo 1").code("CODE1").interestRate(15.0).minimumAmount(152000.0).maximumAmount(162000000.0).automaticValidation(true).build();
    private final ResponseLoanTypeDTO responseLoanTypeDTO = new ResponseLoanTypeDTO("Tipo ",1500.0,154600000.0,15.0,true);
    @Test
    void shouldLoadRequestPathProperties() {
        assertEquals(REQUESTS_PATH, pathsConfig.getRequests());
    }


    @Test
    void testListenPOSTUseCase() {

        when( requestUseCase.saveRequest(request) ).thenReturn(Mono.just(request));
        when(loanTypeUseCasePort.findByCode(loanType.getCode())).thenReturn(Mono.just(loanType));

        when( requestDTOMapper.toResponseDTO( any(Request.class) ) ).thenReturn( responseRequestDTO );
        when( requestDTOMapper.createRequestDTOToRequest( any( CreateRequestDTO.class ),any())).thenReturn( request );


        when(transactionManagement.inTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        webTestClient.post()
                .uri(REQUESTS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.statusCode").isEqualTo(201)
                .jsonPath("$.data.email")
                .value( email -> {
                    Assertions.assertThat(email).isEqualTo(request.getEmail());
                } );
    }
}
