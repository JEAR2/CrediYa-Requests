package co.com.crediya.model.user;

import reactor.core.publisher.Mono;

public interface TokenProvider {
    Mono<String> getCurrentToken();
}
