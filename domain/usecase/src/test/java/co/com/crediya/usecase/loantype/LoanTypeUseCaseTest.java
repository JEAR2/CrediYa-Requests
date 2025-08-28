package co.com.crediya.usecase.loantype;

import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class LoanTypeUseCaseTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanTypeUseCase loanTypeUseCase;

    private LoanType loanType;
    @BeforeEach
    void setUp() {
        loanType = new LoanType().toBuilder().id(1L).name("LoanType 1").code("CREDI1").interestRate(15.0).minimumAmount(12000.0).maximumAmount(26000000.0).automaticValidation(true).build();
    }


    @Test
    void saveRequest_WhenLoanTypeExist_ShouldError() {

        Mockito.when(loanTypeRepository.findByCode(loanType.getCode())).thenReturn(Mono.just(loanType));

        StepVerifier.create(loanTypeUseCase.findByCode(loanType.getCode()))
                .expectNextMatches(responseLoanType -> responseLoanType.getCode().equals( loanType.getCode() ) )
                .verifyComplete();

        verify(loanTypeRepository, times(1)).findByCode(loanType.getCode());
    }

    @Test
    void saveRequest_WhenLoanTypeDoesNotExist_ShouldError() {

        Mockito.when(loanTypeRepository.findByCode(loanType.getCode())).thenReturn(Mono.empty());

        StepVerifier.create(loanTypeUseCase.findByCode(loanType.getCode()))
                .expectError(RequestResourceNotFoundException.class)
                .verify();

        verify(loanTypeRepository, times(1)).findByCode(loanType.getCode());
    }
}