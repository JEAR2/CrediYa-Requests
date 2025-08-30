package co.com.crediya.consumer;


import co.com.crediya.model.user.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.io.IOException;


class RestConsumerTest {
    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;


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
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {

        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function findByEmail returns true when backend says email exists.")
    void validateFindByEmail_WhenExists_ShouldReturnTrue() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("true"));

        var response = restConsumer.findByEmail("acevedo@gmail.com");

        StepVerifier.create(response)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Validate the function findByEmail returns false when backend says email does not exist.")
    void validateFindByEmail_WhenNotExists_ShouldReturnFalse() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("false"));

        var response = restConsumer.findByEmail("notfound@gmail.com");

        StepVerifier.create(response)
                .expectNext(false)
                .verifyComplete();
    }

}