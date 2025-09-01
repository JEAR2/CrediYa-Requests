package co.com.crediya.api.config;

import co.com.crediya.api.dtos.ResponseLoanTypeDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.api.requests.RequestsHandler;
import co.com.crediya.api.requests.RouterRest;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.ports.TransactionManagement;
import co.com.crediya.model.request.Request;
import co.com.crediya.usecase.loantype.LoanTypeUseCase;
import co.com.crediya.usecase.loantype.LoanTypeUseCasePort;
import co.com.crediya.usecase.request.RequestUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@ContextConfiguration(classes = {RouterRest.class, RequestsHandler.class,PathsConfig.class, ValidatorUtil.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TransactionalOperator transactionalOperator;

    @MockitoBean
    private TransactionManagement transactionManagement;

    @MockitoBean
    private RequestUseCase  requestUseCase;
    @MockitoBean
    private LoanTypeUseCase  loanTypeUseCase;
    @MockitoBean
    private ResponseLoanTypeDTO responseLoanTypeDTO;
    @MockitoBean
    private RequestDTOMapper requestDTOMapper;

    private final Request request = Request.builder()
            .id("1").amount(new BigDecimal("10000")).email("a@a.com").period(5).idState(1L).idLoanType(1L).build();

    private final ResponseRequestDTO responseRequestDTO = new ResponseRequestDTO(1L,new BigDecimal("10000"),5,"a@a.com",1L,1L);

    private final LoanType loanType = LoanType.builder().id(1L).name("Type1").code("TYPE1").minimumAmount(1500.0).maximumAmount(350000.0).interestRate(15.0).automaticValidation(true).build();


    @BeforeEach
    void setUp() {
        when(requestUseCase.saveRequest(request,"a@a.com","tokenflaso")).thenReturn(Mono.just(request));
        when(loanTypeUseCase.findByCode(loanType.getCode())).thenReturn(Mono.just(loanType));
        when(requestDTOMapper.toResponseDTO(any())).thenReturn(responseRequestDTO);
    }
    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/v1/requests")
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }
/*
    @Test
    void corsConfigurationShouldAllowOriginsq() {
        webTestClient.mutateWith(
                        mockJwt().jwt(jwt -> jwt.subject("user@test.com"))
                                .authorities(() -> "ROLE_CLIENTE")
                )
                .post()
                .uri("/api/v1/requests")
                .header("Origin", "http://localhost:3000")
                .exchange()
                .expectStatus().isCreated() // o el status real de tu endpoint
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'");
    }*/

}