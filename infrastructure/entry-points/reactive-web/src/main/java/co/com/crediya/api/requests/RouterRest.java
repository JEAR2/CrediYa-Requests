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

import static io.netty.handler.codec.http.HttpMethod.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final RequestsHandler requestRequestsHandler;
    private final ListRequestHandler listRequestHandler;
    private final PathsConfig pathsConfig;
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/requests", produces = {MediaType.APPLICATION_JSON_VALUE,}, method = RequestMethod.POST, beanClass = RequestsHandler.class, beanMethod = "listenSaveRequest")
    })
    public RouterFunction<ServerResponse> routerFunction(RequestsHandler requestsHandler) {
        return route(POST(pathsConfig.getRequests()), this.requestRequestsHandler::listenSaveRequest)
                .andRoute(GET("/api/v1/requests/list"), this.listRequestHandler::listByStates)
                .andRoute(PUT("/api/v1/request/{id}"),this.requestRequestsHandler::listenUpdateStateRequest);
    }


}
