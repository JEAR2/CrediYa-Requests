package co.com.crediya.r2dbc.states;

import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.state.State;
import co.com.crediya.model.state.gateways.StateRepository;
import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import co.com.crediya.r2dbc.entities.StateEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class StateRepositoryAdapter extends ReactiveAdapterOperations<
        State,
        StateEntity,
        Long,
        StateReactiveRepository
        > implements StateRepository {

    public StateRepositoryAdapter(StateReactiveRepository repository,
                                  ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, State.class));
    }

    @Override
    public Flux<State> findByCodeIn(List<String> code) {
        return repository.findByCodeIn(code).map(super::toEntity);
    }

    @Override
    public Mono<State> findByState(String codeState) {
        return repository.findByCode(codeState).map(super::toEntity);
    }
}
