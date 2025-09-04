package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.model.request.Request;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestMapperTest {

    private final CreateRequestDTO  createRequestDTO = new CreateRequestDTO(new BigDecimal("10000"),5,1L,"CODE1");
    private final CreateRequestDTO  createRequestDTO2 = new CreateRequestDTO(BigDecimal.TEN,2,null,null);
    private final CreateRequestDTO  createRequestDTO3 = new CreateRequestDTO(null,null,1L,"CODE1");
    private final Request request = Request.builder().id("1").amount(new BigDecimal("10000")).period(2).email("a@a.com").idState(1L).idLoanType(1L).build();

    private final RequestDTOMapper requestDTOMapper = Mappers.getMapper(RequestDTOMapper.class);

    @Test
    void testCreateRequestToModel() {
        Mono<Request> result = Mono.fromCallable(() -> requestDTOMapper.createRequestDTOToRequest(createRequestDTO, 1L));

        StepVerifier.create(result)
                .expectNextMatches( requestResult ->
                        requestResult.getPeriod().equals(5)
                                && requestResult.getIdState().equals( 1L )
                                && requestResult.getAmount().equals( createRequestDTO.amount() )

                )
                .verifyComplete();
    }
    @Test
    void testCreateRequestToModel_WhenDTOIsNull_ShouldReturnEmptyRequest() {
        Mono<Request> result = Mono.fromCallable(() -> requestDTOMapper.createRequestDTOToRequest(null, 1L));

        StepVerifier.create(result)
                .expectNextMatches(requestResult ->
                        requestResult.getEmail() == null &&
                                requestResult.getAmount() == null &&
                                requestResult.getIdState() == null
                )
                .verifyComplete();
    }

    @Test
    void createRequestToModel_WhenIdStateIsNull_ShouldMapWithoutIdState() {


        Request result = requestDTOMapper.createRequestDTOToRequest(createRequestDTO2, null);

        assertEquals(2, result.getPeriod());
        assertEquals(BigDecimal.TEN, result.getAmount());
        assertNull(result.getIdState());
    }
    @Test
    void createRequestToModel_WhenFieldsAreNull_ShouldReturnRequestWithNulls() {

        Request result = requestDTOMapper.createRequestDTOToRequest(createRequestDTO3, 1L);

        assertNull(result.getPeriod());
        assertNull(result.getAmount());
        assertEquals(1L, result.getIdState());
    }
    @Test
    void createRequestToModel_WhenDTOIsNull_ShouldReturnEmpty() {
        Request result = requestDTOMapper.createRequestDTOToRequest(null, 1L);
        assertNotNull(result);
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
