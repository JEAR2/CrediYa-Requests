package co.com.crediya.model.exceptions.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    LOAN_TYPE_DOES_NOT_EXIST("The loan type does not exist."),
    USER_DOES_NOT_EXIST_IN_THE_SYSTEM("user does not exist in the system"),
    AMOUNT_IS_NOT_WITHIN_PERMITTED_LIMITS("The amount is not within the permitted limits."),
    USER_DOES_NOT_MATCH("The user must be the same as the one who submitted the request."),
    REQUEST_DOES_NOT_EXIST("The request does not exist."),
    STATE_DOES_NOT_EXIST("The state does not exist."),
    CREDENTIALS_NOT_FOUND("Credentials not found."),
    DO_NOT_ACCESS_RESOURCE("Doesn't have access to this resource."),
    UNAUTHORIZED_SENT_TOKEN_INVALID("Sent token is invalid."),
    EXPIRED_TOKEN("Expired token!");
    private String message;

    ExceptionMessages(String message) {
        this.message = message;
    }
}
