package co.com.crediya.consumer;

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



    @Override
    public Mono<Boolean> existsById(String clientId) {
        return null;
    }

    @Override
    @CircuitBreaker(name = "validateEmail")
    public Mono<Boolean> findByEmail(String email) {
        return userWebClient.get()
                .uri("/exists/{email}", email)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
