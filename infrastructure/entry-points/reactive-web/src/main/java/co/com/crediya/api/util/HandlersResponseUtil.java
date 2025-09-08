package co.com.crediya.api.util;

import co.com.crediya.api.exceptions.model.ResponseDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class HandlersResponseUtil {

    public static <T> ResponseDTO<T> buildBodySuccessResponse(String code, T data ) {
        return ResponseDTO
                .<T>builder()
                .message("Operation successful!")
                .data(data)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResponseDTO buildBodyFailureResponse(String code, String message, List<String> errors ) {
        return ResponseDTO
                .builder()
                .message(message)
                .errors(errors)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

}