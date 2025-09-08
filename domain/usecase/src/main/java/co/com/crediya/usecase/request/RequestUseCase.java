package co.com.crediya.usecase.request;

import co.com.crediya.model.exceptions.RequestBadRequestException;
import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;


@RequiredArgsConstructor
public class RequestUseCase implements IRequestUseCase {

    private final RequestRepository requestRepository;
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
        return validateUser(userEmailFromToken)
                .switchIfEmpty(Mono.error(new RequestBadRequestException(
                        ExceptionMessages.USER_DOES_NOT_MATCH.getMessage())))
                .flatMap(exists -> {
                    request.setIdState(request.getIdState());
                    request.setEmail(userEmailFromToken);
                    return requestRepository.save(request);
                });
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
                .flatMap(req -> requestRepository.findRequestsByStateApprovedByUser(req.getEmail(), "APPROVED")
                        .map(r -> calculateMonthlyPayment(req.getAmount(), req.getPeriod(), req.getInterestRate()))
                        .reduce(0.0,Double::sum)
                        .map(totalDebt -> {
                            req.setTotalMonthlyDebtApprovedRequests(totalDebt);
                            return req;
                        }));
    }


        private double calculateMonthlyPayment(double amount, int period, double annualRate) {
            double total = amount + (amount * (annualRate / 100.0));
            double monthly = total / period;
            return Math.round(monthly * 100.0) / 100.0;

        }

}
