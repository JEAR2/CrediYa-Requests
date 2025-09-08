package co.com.crediya.model.user;

import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<Boolean> existsById(String clientId);
    Mono<User> findByEmail(String email);
}
