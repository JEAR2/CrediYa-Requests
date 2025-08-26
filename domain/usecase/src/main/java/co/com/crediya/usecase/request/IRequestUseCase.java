package co.com.crediya.usecase.request;

import co.com.crediya.model.request.Request;
import reactor.core.publisher.Mono;

public interface IRequestUseCase {
    Mono<Request> saveRequest(Request request);
}
