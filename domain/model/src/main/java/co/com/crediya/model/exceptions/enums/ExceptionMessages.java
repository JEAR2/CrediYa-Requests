package co.com.crediya.model.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    LOAN_TYPE_DOES_NOT_EXIST("The loan type does not exist."),
    USER_DOES_NOT_EXIST_IN_THE_SYSTEM("user does not exist in the system"),
    AMOUNT_IS_NOT_WITHIN_PERMITTED_LIMITS("The amount is not within the permitted limits.");

    private String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
}
