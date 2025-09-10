package co.com.crediya.usecase.request;

import co.com.crediya.model.request.Request;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRequestUseCase {
    Mono<Request> saveRequest(Request request, String userEmailFromToken);
    Flux<Request> findRequestByState(List<String> state, int page, int size);
    Mono<Request> updateStateRequest(String id, String codeState);
}
