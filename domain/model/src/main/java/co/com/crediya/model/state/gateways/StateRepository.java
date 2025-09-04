package co.com.crediya.model.state.gateways;

import co.com.crediya.model.state.State;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StateRepository {

    Mono<State> findById(Long id);
    Flux<State> findByCodeIn(List<String> codes);
}
