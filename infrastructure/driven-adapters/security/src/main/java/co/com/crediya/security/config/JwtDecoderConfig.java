package co.com.crediya.security.config;


import co.com.crediya.security.enums.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    @Value("${security.jwt.public-key-location}")
    private String publicKeyResource;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {

        String keyContent = publicKeyResource
                .replace(SecurityConstants.REGEX_START_PUBLIC_KEY.getValue(), "")
                .replace(SecurityConstants.REGEX_END_PUBLIC_KEY.getValue(), "")
                .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

        byte[] decoded = Base64.getDecoder().decode(keyContent);

        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue())
                .generatePublic(new X509EncodedKeySpec(decoded));

        return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();

    }
}


