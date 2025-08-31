package co.com.crediya.api.requests;

import co.com.crediya.api.config.PathsConfig;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final RequestsHandler requestRequestsHandler;
    private final PathsConfig pathsConfig;
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/requests", produces = {MediaType.APPLICATION_JSON_VALUE,}, method = RequestMethod.POST, beanClass = RequestsHandler.class, beanMethod = "listenSaveRequest")
    })
    public RouterFunction<ServerResponse> routerFunction(RequestsHandler requestsHandler) {
        return route(POST(pathsConfig.getRequests()), this.requestRequestsHandler::listenSaveRequest);
    }
}
