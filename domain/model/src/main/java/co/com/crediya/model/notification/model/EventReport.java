package co.com.crediya.model.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventReport {
    private String requestId;
    private BigDecimal amount;
    private String state;
    private String createAt;
}
