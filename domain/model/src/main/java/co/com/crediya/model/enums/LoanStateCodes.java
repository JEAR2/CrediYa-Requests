package co.com.crediya.model.enums;

import lombok.Getter;

@Getter
public enum LoanStateCodes {

    APPROVED("APPROVED");


    private final String status;

    LoanStateCodes(String status) {
        this.status = status;
    }
}
