package co.com.crediya.r2dbc.Loan;

import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, Long> , ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanTypeEntity> findByCode(String code);
}
