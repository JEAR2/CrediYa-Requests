package co.com.crediya.api.dtos;

public record ResponseLoanTypeDTO(String name,
        Double minimumAmount,
        Double maximumAmount,
        Double interestRate,
        Boolean automaticValidation ) {
}
