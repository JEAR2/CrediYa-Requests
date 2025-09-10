package co.com.crediya.model.request.gateways;

import co.com.crediya.model.request.Request;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RequestRepository {
    Mono<Request> save(Request request);
    Flux<Request> findRequestsByState(List<Long> states, int page, int size);
    Flux<Request> findRequestsByStateApprovedByUser(String email,String state);
    Mono<Request> findById(String id);
}
