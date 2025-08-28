package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.model.request.Request;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestMapperTest {

    private final CreateRequestDTO  createRequestDTO = new CreateRequestDTO(1500.0,5,"a@a.com",1L,"CODE1");

    private final Request request = Request.builder().id("1").amount(150.0).period(2).email("a@a.com").idState(1L).idLoanType(1L).build();

    private final RequestDTOMapper requestDTOMapper = Mappers.getMapper(RequestDTOMapper.class);

    @Test
    void testCreateRequestToModel() {
        Mono<Request> result = Mono.fromCallable(() -> requestDTOMapper.createRequestDTOToRequest(createRequestDTO, 1L));

        StepVerifier.create(result)
                .expectNextMatches( requestResult ->
                        requestResult.getEmail().equals(createRequestDTO.email())
                                && requestResult.getIdState().equals( 1L )
                                && requestResult.getAmount().equals( createRequestDTO.amount() )

                )
                .verifyComplete();
    }


    @Test
    void testModelToResponse() {
        Mono<ResponseRequestDTO> result = Mono.fromCallable(() -> requestDTOMapper.toResponseDTO(request));

        StepVerifier.create(result)
                .expectNextMatches( requestResponseResult ->
                                requestResponseResult.amount().equals(request.getAmount())
                                && requestResponseResult.period().equals(request.getPeriod())
                                && requestResponseResult.email().equals(request.getEmail())
                                && requestResponseResult.idState().equals(request.getIdState())
                                && requestResponseResult.idLoanType().equals( request.getIdLoanType())
                )
                .verifyComplete();
    }

}
