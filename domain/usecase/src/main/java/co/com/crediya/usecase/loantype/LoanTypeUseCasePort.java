package co.com.crediya.usecase.loantype;

import co.com.crediya.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeUseCasePort {
    Mono<LoanType> findByCode(String  code);
}
