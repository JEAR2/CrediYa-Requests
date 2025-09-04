package co.com.crediya.r2dbc.Loan;

import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StateRepositoryAdapterTest {

    @InjectMocks
    LoanTypeRepositoryAdapter  loanTypeRepositoryAdapter;

    @Mock
    LoanTypeReactiveRepository  loanTypeRepository;

    @Mock
    ObjectMapper objectMapper;

    private LoanType loanType;

    private LoanTypeEntity loanTypeEntity;

    @BeforeEach
    void setUp() {
        loanType = LoanType.builder()
                .id(1L)
                .name("Type Loan One")
                .minimumAmount(12.0)
                .maximumAmount(52000.0)
                .interestRate(5.0)
                .automaticValidation(true)
                .build();
        loanTypeEntity = LoanTypeEntity.builder()
                .id(1L)
                .name("Type Loan One")
                .minimumAmount(12.0)
                .maximumAmount(52000.0)
                .interestRate(5.0)
                .automaticValidation(true)
                .build();
    }
    @Test
    @DisplayName("Must find type loan by id")
    void findByIdWhenLoanTypeExists_ShouldMappedLoanTypeEntity() {
        // Arrange
        when(loanTypeRepository.findById(loanTypeEntity.getId())).thenReturn(Mono.just(loanTypeEntity));
        when(objectMapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);
        // Act
        Mono<LoanType> result = loanTypeRepositoryAdapter.findById(loanTypeEntity.getId());
        // Assert
        StepVerifier.create(result)
                .expectNextMatches( loanTypeFound -> loanTypeFound.getId().equals(loanTypeEntity.getId()) )
                .verifyComplete();
    }
}