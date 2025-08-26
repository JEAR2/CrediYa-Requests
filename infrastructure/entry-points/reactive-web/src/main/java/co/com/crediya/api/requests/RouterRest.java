package co.com.crediya.api.requests;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/requests",
                    produces = {
                            MediaType.APPLICATION_JSON_VALUE,
                    },
                    method = RequestMethod.POST,
                    beanClass = RequestsHandler.class,
                    beanMethod = "listenSaveRequest",
                    operation = @Operation( tags = "Requests", operationId = "saveRequest", description = "Save a request", summary = "Save a request",
                            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateRequestDTO.class ) ) ),
                            responses = { @ApiResponse( responseCode = "201", description = "request saved successfully.", content = @Content( schema = @Schema( implementation = ResponseRequestDTO.class ) ) ),
                                    //@ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = CustomError.class ) ) )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(RequestsHandler requestsHandler) {
        return route(POST("/api/v1/requests"), this.requestRequestsHandler::listenSaveRequest);
    }
}
