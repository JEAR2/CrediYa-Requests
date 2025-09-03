package co.com.crediya.security.config;


import co.com.crediya.security.enums.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    @Value("${security.jwt.public-key-location}")
    private Resource publicKeyResource;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        try (InputStream is = publicKeyResource.getInputStream()) {
            String keyContent = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace(SecurityConstants.REGEX_START_PUBLIC_KEY.getValue(), "")
                    .replace(SecurityConstants.REGEX_END_PUBLIC_KEY.getValue(), "")
                    .replaceAll(SecurityConstants.REGEX_SPACES.getValue(), "");

            byte[] decoded = Base64.getDecoder().decode(keyContent);

            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance(SecurityConstants.TYPE_ALGORITHM.getValue())
                    .generatePublic(new X509EncodedKeySpec(decoded));

            return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
        }
    }
}


