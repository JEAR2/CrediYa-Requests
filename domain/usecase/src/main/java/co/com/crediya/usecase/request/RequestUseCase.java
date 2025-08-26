package co.com.crediya.usecase.request;

import co.com.crediya.model.loantype.LoanType;
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
    private final LoanTypeRepository loanTypeRepository;

    @Override
    public Mono<Request> saveRequest(Request request) {
        return validarUsuario(request.getEmail())
                .then(loanTypeRepository.findById(request.getIdLoanType())
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("El tipo de préstamo no existe")))
                        .then(Mono.defer(() -> {
                            request.setIdState(1L);
                            return requestRepository.save(request);
                        }))
                );

    }

    private Mono<Request> validarUsuario(String email) {
        return userGateway.findByEmail(email)
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new IllegalArgumentException("El usuario no existe en el sistema"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Request> validarMonto(Request solicitud, LoanType tipoPrestamo) {
        if (solicitud.getAmount() < tipoPrestamo.getMinimumAmount() ||
                solicitud.getAmount() > tipoPrestamo.getMaximumAmount()) {
            return Mono.error(new IllegalArgumentException("El monto no está dentro de los límites permitidos"));
        }
        return Mono.just(solicitud.toBuilder().idLoanType(tipoPrestamo.getId()).build());
    }


}
