package co.com.crediya.r2dbc.Loan;

import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTypeRepositoryAdapterTest {

    @Mock
    private LoanTypeReactiveRepository loanTypeReactiveRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LoanTypeRepositoryAdapter loanTypeRepositoryAdapter;

    private LoanType loanType;
    private LoanTypeEntity loanTypeEntity;

    @BeforeEach
    void setUp() {
        loanTypeEntity = LoanTypeEntity.builder()
                .id(1L)
                .code("HOME")
                .name("Home Loan")
                .interestRate(5.5)
                .build();

        loanType = LoanType.builder()
                .id(1L)
                .code("HOME")
                .name("Home Loan")
                .interestRate(15.0)
                .build();
    }

    @Test
    void findByCode_WhenExists_ShouldReturnLoanType() {
        // Arrange
        when(loanTypeReactiveRepository.findByCode("HOME"))
                .thenReturn(Mono.just(loanTypeEntity));
        when(objectMapper.map(loanTypeEntity, LoanType.class))
                .thenReturn(loanType);

        // Act
        Mono<LoanType> result = loanTypeRepositoryAdapter.findByCode("HOME");

        // Assert
        StepVerifier.create(result)
                .expectNext(loanType)
                .verifyComplete();

        verify(loanTypeReactiveRepository, times(1)).findByCode("HOME");
        verify(objectMapper, times(1)).map(loanTypeEntity, LoanType.class);
    }

    @Test
    void findByCode_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(loanTypeReactiveRepository.findByCode("CAR"))
                .thenReturn(Mono.empty());

        // Act
        Mono<LoanType> result = loanTypeRepositoryAdapter.findByCode("CAR");

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(loanTypeReactiveRepository, times(1)).findByCode("CAR");
        verify(objectMapper, never()).map(any(), eq(LoanType.class));
    }
}