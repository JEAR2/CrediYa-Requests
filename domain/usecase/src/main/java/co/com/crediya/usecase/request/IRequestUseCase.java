package co.com.crediya.usecase.request;

import co.com.crediya.model.request.PageRequestModel;
import co.com.crediya.model.request.Request;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IRequestUseCase {
    Mono<Request> saveRequest(Request request, String userEmailFromToken, String token);
    Flux<Request> findRequestByState(List<Long> estados, int page, int size);

}
