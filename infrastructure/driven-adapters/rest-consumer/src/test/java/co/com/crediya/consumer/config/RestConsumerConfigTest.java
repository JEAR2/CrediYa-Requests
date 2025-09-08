package co.com.crediya.consumer.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RestConsumerConfigTest {
    @Test
    void shouldBuildWebClientWithBaseUrlAndHeaders() {
        RestConsumerConfig config = new RestConsumerConfig("http://localhost:8080", 5000);

        ExchangeFunction mockExchange = request -> {
            assertThat(request.url().toString())
                    .startsWith("http://localhost:8080");
            assertThat(request.headers().getFirst(HttpHeaders.CONTENT_TYPE))
                    .isEqualTo("application/json");
            return Mono.empty();
        };

        WebClient webClient = config.getWebClient(WebClient.builder().exchangeFunction(mockExchange));

        webClient.get().uri("/requests").retrieve();
    }
}
