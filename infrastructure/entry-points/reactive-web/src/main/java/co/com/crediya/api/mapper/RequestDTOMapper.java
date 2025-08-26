package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.CreateRequestDTO;
import co.com.crediya.api.dtos.ResponseRequestDTO;
import co.com.crediya.model.request.Request;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestDTOMapper {
    ResponseRequestDTO toResponseDTO(Request request);
    Request toRequest(CreateRequestDTO  createRequestDTO);
}
