package co.com.crediya.consumer;


import co.com.crediya.model.user.TokenProvider;
import co.com.crediya.model.user.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RestConsumerTest {
    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;

    private static TokenProvider mockTokenProvider;


    private final User user = User.builder()
            .name("john")
            .lastName("acevedo")
            .identityDocument("123")
            .email("arevalo@gmail.com")
            .build();

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        mockTokenProvider = Mockito.mock(TokenProvider.class);
        Mockito.when(mockTokenProvider.getCurrentToken())
                .thenReturn(Mono.just("fake-jwt-token"));
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient,mockTokenProvider);
    }

    @AfterAll
    static void tearDown() throws IOException {

        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function findByEmail returns user when backend says email exists.")
    void validateFindByEmail_WhenExists_ShouldReturnUser() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"name\":\"john\",\"lastName\":\"acevedo\",\"identityDocument\":\"123\",\"email\":\"acevedo@gmail.com\"}"));

        var response = restConsumer.findByEmail("acevedo@gmail.com");

        StepVerifier.create(response)
                .assertNext(user -> {
                    assertEquals("john", user.getName());
                    assertEquals("acevedo", user.getLastName());
                    assertEquals("123", user.getIdentityDocument());
                    assertEquals("acevedo@gmail.com", user.getEmail());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Validate the function findByEmail returns empty when backend says email not exists.")
    void validateFindByEmail_WhenNotExists_ShouldReturnEmpty() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.NOT_FOUND.value()));

        var response = restConsumer.findByEmail("notfound@gmail.com");

        StepVerifier.create(response)
                .expectError(WebClientResponseException.NotFound.class)
                .verify();
    }


}