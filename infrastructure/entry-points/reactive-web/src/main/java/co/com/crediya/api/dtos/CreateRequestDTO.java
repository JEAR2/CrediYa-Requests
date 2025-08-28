package co.com.crediya.api.dtos;

public record CreateRequestDTO (
        Double amount,
        Integer period,
        String email,
        Long idState,
        String codeLoanType){
}
