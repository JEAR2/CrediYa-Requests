package co.com.crediya.usecase.request;

import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class RequestUseCase implements IRequestUseCase {

    private final RequestRepository requestRepository;
    private final UserGateway userGateway;

    @Override
    public Mono<Request> saveRequest(Request request) {
        return validateUser(request.getEmail())
                .flatMap(valid -> {
                    request.setIdState(1L);
                    return requestRepository.save(request);
                });
    }

    private Mono<Boolean> validateUser(String email) {
        return userGateway.findByEmail(email)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(
                        ExceptionMessages.USER_DOES_NOT_EXIST_IN_THE_SYSTEM.getMessage()
                )));
    }



}
