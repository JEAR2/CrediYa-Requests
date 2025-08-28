package co.com.crediya.model.ports;

import reactor.core.publisher.Mono;

public interface TransactionManagement {
    public <T> Mono<T> inTransaction(Mono<T> action);
}
