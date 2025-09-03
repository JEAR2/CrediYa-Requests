package co.com.crediya.r2dbc.requests;

import co.com.crediya.r2dbc.entities.RequestsEntity;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;


public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestsEntity, String>, ReactiveQueryByExampleExecutor<RequestsEntity> {

    Flux<RequestsEntity> findByIdStateIn(List<Long> estados, Pageable pageable);
}
