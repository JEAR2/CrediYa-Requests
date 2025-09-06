package co.com.crediya.model.loantype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    Long id;
    String name;
    String code;
    Double minimumAmount;
    Double maximumAmount;
    Double interestRate;
    Boolean automaticValidation;

}
