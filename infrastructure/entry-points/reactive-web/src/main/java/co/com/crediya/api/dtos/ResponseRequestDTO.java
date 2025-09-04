package co.com.crediya.api.dtos;

import java.math.BigDecimal;

public record ResponseRequestDTO(BigDecimal amount,
                                 Integer period,
                                 String email,
                                 Long idState,
                                 Long idLoanType,
                                 String state,
                                 String type,
                                 BigDecimal basePayment,
                                 String nameClient,
                                 BigDecimal interestRate) {
}
