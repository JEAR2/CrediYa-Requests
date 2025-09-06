package co.com.crediya.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseRequestDTO(Double amount,
                                 Integer period,
                                 String email,
                                 Long idState,
                                 Long idLoanType,
                                 String state,
                                 String type,
                                 Double basePayment,
                                 String nameClient,
                                 Double interestRate,
                                 Double totalMonthlyDebtApprovedRequests) {
}
