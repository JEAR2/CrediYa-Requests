package co.com.crediya.api.requests;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.api.exceptions.model.ResponseDTO;
import co.com.crediya.api.mapper.RequestDTOMapper;
import co.com.crediya.api.util.HandlersResponseUtil;
import co.com.crediya.api.util.ValidatorUtil;
import co.com.crediya.model.exceptions.RequestResourceNotFoundException;
import co.com.crediya.model.exceptions.enums.ExceptionMessages;
import co.com.crediya.model.exceptions.enums.ExceptionStatusCode;
import co.com.crediya.model.ports.TransactionManagement;
import co.com.crediya.usecase.loantype.LoanTypeUseCasePort;
import co.com.crediya.usecase.request.RequestUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestsHandler {
    private final RequestUseCase requestUseCase;
    private final LoanTypeUseCasePort  loanTypeUseCasePort;
    private final RequestDTOMapper requestDTOMapper;
    private final TransactionManagement transactionManagement;
    private final ValidatorUtil validatorUtil;

    @Operation( tags = "Requests", operationId = "saveRequest", description = "Save a request", summary = "Save a request",
            requestBody = @RequestBody( content = @Content( schema = @Schema( implementation = CreateRequestDTO.class ) ) ),
            responses = { @ApiResponse( responseCode = "201", description = "Request saved successfully.", content = @Content( schema = @Schema( implementation = ResponseRequestDTO.class ) ) ),
                    @ApiResponse( responseCode = "400", description = "Request body is not valid.", content = @Content( schema = @Schema( implementation = ResponseDTO.class ) ) ),
                    @ApiResponse( responseCode = "404", description = "User email sent is not found.", content = @Content( schema = @Schema( implementation = ResponseDTO.class ) ) )
            }
    )
    public Mono<ServerResponse> listenSaveRequest(ServerRequest serverRequest) {

        String rawToken = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        String token = (rawToken != null && rawToken.startsWith("Bearer "))
                ? rawToken.substring(7)
                : rawToken;


        return serverRequest.principal()
                .cast(JwtAuthenticationToken.class)
                .map(auth -> auth.getToken().getSubject())
                .flatMap(userEmailFromToken ->
                        serverRequest.bodyToMono(CreateRequestDTO.class)
                                .flatMap(validatorUtil::validate)
                                .flatMap(dto ->
                                        loanTypeUseCasePort.findByCode(dto.codeLoanType())
                                                .map(loanType -> requestDTOMapper.createRequestDTOToRequest(dto, loanType.getId()))
                                                .flatMap(request ->
                                                        transactionManagement.inTransaction(
                                                                requestUseCase.saveRequest(request, userEmailFromToken, token)
                                                        )
                                                )
                                                .map(request -> {
                                                    request.setEmail(userEmailFromToken);
                                                    return request;
                                                })
                                                .map(requestDTOMapper::toResponseDTO)
                                )
                                .flatMap(savedRequest ->
                                        ServerResponse.created(URI.create("/requests/" + userEmailFromToken))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(
                                                        ExceptionStatusCode.CREATED.status(),
                                                        savedRequest
                                                ))
                                )
                );
    }





}
