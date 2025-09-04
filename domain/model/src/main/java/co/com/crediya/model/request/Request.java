package co.com.crediya.model.request;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {
    private String id;
    private BigDecimal amount;
    private Integer period;
    private String email;
    private Long idState;
    private Long idLoanType;

    private String state;
    private String type;
    private BigDecimal basePayment;
    private String nameClient;
    private BigDecimal interestRate;
}
