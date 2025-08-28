package co.com.crediya.api.exceptions;

import co.com.crediya.api.exceptions.model.CustomError;
import co.com.crediya.api.util.HandlersUtil;
import co.com.crediya.model.exceptions.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        if( !(error instanceof RequestException customException) ) {
            return super.getErrorAttributes(request, options);
        }
        CustomError customError = CustomError.builder()
                .statusCode(customException.getStatusCode().getStatusCode())
                .message(customException.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return  HandlersUtil.buildBodyResponse(false, customException.getStatusCode().getStatusCode(), "error", customError);
    }
}
