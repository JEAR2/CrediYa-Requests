package co.com.crediya.api.exceptions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomError {
    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;
}
