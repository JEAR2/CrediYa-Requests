package co.com.crediya.security.enums;

import lombok.Getter;

@Getter
public enum SecurityConstants {
    ROLE_CLAIM("role"),
    REGEX_START_PUBLIC_KEY("-----BEGIN PUBLIC KEY-----"),
    REGEX_END_PUBLIC_KEY("-----END PUBLIC KEY-----"),
    REGEX_SPACES("\\s"),
    TYPE_ALGORITHM("RSA"),
    PREFIX_ROLE_AUTH("ROLE_");

    private final String value;
    SecurityConstants(String value) {
        this.value = value;
    }
}
