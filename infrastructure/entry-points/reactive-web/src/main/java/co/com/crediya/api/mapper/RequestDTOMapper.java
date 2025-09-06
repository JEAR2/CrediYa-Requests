package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.model.request.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestDTOMapper {

    @Mapping(
            source = "idLoanType",
            target = "idLoanType"
    )
    Request createRequestDTOToRequest(CreateRequestDTO  createRequestDTO, Long idLoanType);
    ResponseRequestDTO toResponseDTO(Request request);

}
