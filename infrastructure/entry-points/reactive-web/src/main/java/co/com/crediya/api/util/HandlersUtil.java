package co.com.crediya.api.util;

import java.time.LocalDateTime;
import java.util.Map;

public class HandlersUtil {

    public static Map<String, Object> buildBodyResponse(Boolean state, Integer statusCode, String keyData, Object data ) {
        return Map.of(
                "success", state,
                "statusCode", statusCode,
                "timestamp", LocalDateTime.now(),
                keyData, data
        );
    }
}
