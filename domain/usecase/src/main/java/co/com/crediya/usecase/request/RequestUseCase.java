package co.com.crediya.usecase.request;

import co.com.crediya.model.enums.LoanStateCodes;
import co.com.crediya.model.exceptions.RequestBadRequestException;
import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.notification.QueuePort;
import co.com.crediya.model.notification.model.AutoValidationPayload;
import co.com.crediya.model.notification.model.EventReport;
import co.com.crediya.model.notification.model.MessageNotification;
import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.model.state.State;
import co.com.crediya.model.state.gateways.StateRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
public class RequestUseCase implements IRequestUseCase {

    private final RequestRepository requestRepository;
    private final QueuePort  queuePort;
    private final StateRepository stateRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserGateway userGateway;

    private Mono<User> validateUser(String email) {
        return userGateway.findByEmail(email)
                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(
                        ExceptionMessages.USER_DOES_NOT_EXIST_IN_THE_SYSTEM.getMessage()
                )));
    }

    @Override
    public Mono<Request> saveRequest(Request request, String userEmailFromToken) {
        return validateAndGetUser(userEmailFromToken)
                .flatMap(user -> validateLoanType(request.getIdLoanType())
                        .flatMap(loanType -> processRequest(request, userEmailFromToken, user, loanType)));
    }

    private Mono<User> validateAndGetUser(String userEmail) {
        return validateUser(userEmail)
                .switchIfEmpty(Mono.error(new RequestBadRequestException(
                        ExceptionMessages.USER_DOES_NOT_MATCH.getMessage())));
    }

    private Mono<LoanType> validateLoanType(Long loanTypeId) {
        return loanTypeRepository.findById(loanTypeId)
                .switchIfEmpty(Mono.error(new RequestBadRequestException(
                        ExceptionMessages.LOAN_TYPE_DOES_NOT_EXIST.getMessage())));
    }

    private Mono<Request> processRequest(Request request, String userEmailFromToken,
                                         User user, LoanType loanType) {
        request.setEmail(userEmailFromToken);
        return requestRepository.save(request)
                .flatMap(saved -> handleAutoValidation(saved, user, loanType));
    }

    private Mono<Request> handleAutoValidation(Request saved, User user, LoanType loanType) {
        boolean autoValidation = Boolean.TRUE.equals(loanType.getAutomaticValidation());
        if (!autoValidation) {
            return Mono.just(saved);
        }

        return computeTotalMonthlyDebt(saved.getEmail())
                .defaultIfEmpty(0.0)
                .flatMap(totalDebt -> publishAutoValidation(saved, user, loanType, totalDebt));
    }

    private Mono<Request> publishAutoValidation(Request saved, User user,
                                                LoanType loanType, Double totalDebt) {
        AutoValidationPayload payload = AutoValidationPayload.builder()
                .requestId(String.valueOf(saved.getId()))
                .applicantId(saved.getEmail())
                .salary(user.getBaseSalary())
                .amount(saved.getAmount())
                .annualRate(loanType.getInterestRate())
                .termMonths(saved.getPeriod())
                .CurrentMonthlyDebt(totalDebt)
                .email(saved.getEmail())
                .traceId(UUID.randomUUID().toString())
                .build();

        return queuePort.publishAutoValidation(payload)
                .thenReturn(saved);
    }

    @Override
    public Flux<Request> findRequestByState(List<String> states, int page, int size) {

        return stateRepository.findByCodeIn(states)
                .map(State::getId)
                .collectList()
                .flatMapMany(statesIds ->
                        requestRepository.findRequestsByState(statesIds, page, size)
                )
                .flatMap(request -> {
                    Mono<State> stateMono = stateRepository.findById(request.getIdState());
                    Mono<LoanType> loanTypeMono = loanTypeRepository.findById(request.getIdLoanType());
                    Mono<User> userMono = userGateway.findByEmail(request.getEmail());
                    return Mono.zip(stateMono, loanTypeMono, (state, loadType) -> {
                        request.setState(state.getName());
                        request.setType(loadType.getName());
                        request.setInterestRate(loadType.getInterestRate());
                        return request;
                    }).zipWith(userMono, (requestAll, user) -> {
                        requestAll.setBasePayment(user.getBaseSalary());
                        requestAll.setNameClient(user.getName());
                        return requestAll;
                    });
                })
                .flatMap(req -> requestRepository.findRequestsByStateApprovedByUser(req.getEmail(), LoanStateCodes.APPROVED.getStatus())
                        .map(r -> calculateMonthlyPayment(req.getAmount(), req.getPeriod(), req.getInterestRate()))
                        .reduce(0.0,Double::sum)
                        .map(totalDebt -> {
                            req.setTotalMonthlyDebtApprovedRequests(totalDebt);
                            return req;
                        }));
    }

@Override
public Mono<Request> updateStateRequest(String id, String state) {

    Mono<Request> requestMono = requestRepository.findById(id)
            .switchIfEmpty(Mono.error(
                    new RequestResourceNotFoundException(ExceptionMessages.REQUEST_DOES_NOT_EXIST.getMessage())
            ));

    Mono<State> stateMono = stateRepository.findByState(state)
            .switchIfEmpty(Mono.error(
                    new RequestResourceNotFoundException(ExceptionMessages.STATE_DOES_NOT_EXIST.getMessage())
            ));

    return requestMono.zipWith(stateMono)
            .flatMap(tuple -> {
                Request request = tuple.getT1();
                State stateNew = tuple.getT2();

                request.setIdState(stateNew.getId());

                return requestRepository.save(request);
            })
            .flatMap(savedRequest -> {
                Mono<Void> notifyAllStates = queuePort.publishChangeStatus(
                        MessageNotification.builder()
                                .idRequest(savedRequest.getId())
                                .state(state)
                                .email(savedRequest.getEmail())
                                .build()
                );

                Mono<Void> notifyApproved = Mono.empty();
                if (LoanStateCodes.APPROVED.getStatus().equalsIgnoreCase(state)) {
                    notifyApproved = queuePort.publishStatusApprovedReport(
                            EventReport.builder()
                                    .requestId(String.valueOf(savedRequest.getId()))
                                    .state(state)
                                    .amount(BigDecimal.valueOf(savedRequest.getAmount()))
                                    .createdAt(LocalDateTime.now().toString())
                                    .build()
                    );
                }

                return Mono.when(notifyAllStates, notifyApproved)
                        .thenReturn(savedRequest);
            });
}


    @Override
    public Mono<Request> updateStateRequestWithOutEmail(String id, String codeState) {
        return requestRepository.findById(id)
                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(ExceptionMessages.REQUEST_DOES_NOT_EXIST.getMessage())))
                .flatMap(request ->
                        stateRepository.findByState(codeState)
                                .switchIfEmpty(Mono.error(new RequestResourceNotFoundException(ExceptionMessages.STATE_DOES_NOT_EXIST.getMessage())))
                                .flatMap(stateNew -> {
                                    request.setIdState(stateNew.getId());

                                    return requestRepository.save(request);
                                })
                );

    }

    private Mono<Double> computeTotalMonthlyDebt(String email) {
        return requestRepository.findRequestsByStateApprovedByUser(email, LoanStateCodes.APPROVED.getStatus())
                .flatMap(request -> loanTypeRepository.findById(request.getIdLoanType())
                        .map(loanType -> {
                            request.setInterestRate(loanType.getInterestRate());
                            return request;
                        }))
                .map(r -> calculateMonthlyPayment(r.getAmount(), r.getPeriod(), r.getInterestRate()))
                .reduce(0.0, Double::sum);
    }


    private double calculateMonthlyPayment(double amount, int period, double annualRate) {
        double monthlyRate = (annualRate / 100.0) / 12;
        double quota = (amount * monthlyRate) /
                (1 - Math.pow(1 + monthlyRate, -period));
        return Math.round(quota * 100.0) / 100.0;
    }


}
