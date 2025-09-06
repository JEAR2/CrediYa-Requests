package co.com.crediya.r2dbc.requests;

import co.com.crediya.r2dbc.entities.RequestsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;


public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestsEntity, String>, ReactiveQueryByExampleExecutor<RequestsEntity> {

    Flux<RequestsEntity> findByIdStateIn(List<Long> states, Pageable pageable);

    @Query("""
        SELECT r.* 
        FROM requests r
        JOIN state s ON r.id_state = s.id
        WHERE r.email = :email
          AND s.code = :approvedCode
    """)

    Flux<RequestsEntity> findRequestsByStateApprovedByUser(String email, String state);

}
