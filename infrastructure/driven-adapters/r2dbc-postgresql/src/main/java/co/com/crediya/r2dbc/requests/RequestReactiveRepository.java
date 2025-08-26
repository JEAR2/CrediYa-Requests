package co.com.crediya.r2dbc.requests;

import co.com.crediya.r2dbc.entities.RequestsEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestsEntity, Long>, ReactiveQueryByExampleExecutor<RequestsEntity> {

}
