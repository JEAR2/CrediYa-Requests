package co.com.crediya.api.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateRequestDTO (
        @NotNull(message = "is required!")
        BigDecimal amount,
        @NotNull(message = "is required!")
        Integer period,
        @NotNull(message = "is required!")
        String email,
        @NotNull(message = "is required!")
        Long idState,
        @NotNull(message = "is required!")
        String codeLoanType){
}
