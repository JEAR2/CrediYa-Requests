package co.com.crediya.api.dtos;


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
