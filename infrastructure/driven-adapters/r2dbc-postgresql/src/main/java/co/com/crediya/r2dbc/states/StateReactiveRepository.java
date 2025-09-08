package co.com.crediya.r2dbc.states;

import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import co.com.crediya.r2dbc.entities.StateEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StateReactiveRepository extends ReactiveCrudRepository<StateEntity, Long> , ReactiveQueryByExampleExecutor<StateEntity> {
    Flux<StateEntity> findByCodeIn(List<String> codes);
}
