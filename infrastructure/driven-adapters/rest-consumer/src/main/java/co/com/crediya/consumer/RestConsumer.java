package co.com.crediya.consumer;

import co.com.crediya.model.user.TokenProvider;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.UserGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserGateway{
    private final WebClient userWebClient;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<Boolean> existsById(String clientId) {
        return null;
    }

    @Override
    @CircuitBreaker(name = "validateEmail")
    public Mono<User> findByEmail(String email) {

        return tokenProvider.getCurrentToken()
                        .flatMap(token -> userWebClient.get()
                                .uri("/email/{email}", email)
                                .headers(headers -> headers.setBearerAuth(token))
                                .retrieve()
                                .bodyToMono(User.class));
    }
}
