package co.com.crediya.usecase.loantype;

import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanTypeUseCase implements LoanTypeUseCasePort {

    private final LoanTypeRepository loanTypeRepository;

    @Override
    public Mono<LoanType> findByCode(String code) {
        return loanTypeRepository.findByCode(code)
                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(String.format(ExceptionMessages.LOAN_TYPE_DOES_NOT_EXIST.getMessage(),code))));
    }
}
