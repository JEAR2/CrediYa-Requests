package co.com.crediya.api.dtos;

public record ResponseRequestDTO(Long id,
                                Double amount,
                                 Integer period,
                                 String email,
                                 Long idState,
                                 Long idLoanType) {
}
