package co.com.crediya.model.request.gateways;

import co.com.crediya.model.request.Request;
import reactor.core.publisher.Mono;

public interface RequestRepository {
    Mono<Request> save(Request request);
    Mono<Request> findById(String id);
}
