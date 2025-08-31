package co.com.crediya.usecase.request;

import co.com.crediya.model.exceptions.RequestBadRequestException;
import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class RequestUseCase implements IRequestUseCase {

    private final RequestRepository requestRepository;
    private final UserGateway userGateway;

    private Mono<Boolean> validateUser(String email, String token) {
        return userGateway.findByEmail(email,token)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(
                        ExceptionMessages.USER_DOES_NOT_EXIST_IN_THE_SYSTEM.getMessage()
                )));
    }


    @Override
    public Mono<Request> saveRequest(Request request, String userEmailFromToken, String token) {
        return validateUser(request.getEmail(), token)
                .filter(exists -> userEmailFromToken.equalsIgnoreCase(request.getEmail()))
                .switchIfEmpty(Mono.error(new RequestBadRequestException(
                        ExceptionMessages.USER_DOES_NOT_MATCH.getMessage())))
                .flatMap(exists -> {
                    request.setIdState(1L);
                    return requestRepository.save(request);
                });
    }



}
