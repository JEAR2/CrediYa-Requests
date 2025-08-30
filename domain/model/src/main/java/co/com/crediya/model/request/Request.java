package co.com.crediya.model.request;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {
    String id;
    BigDecimal amount;
    Integer period;
    String email;
    Long idState;
    Long idLoanType;
}
