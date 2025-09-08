package co.com.crediya.security.helper;

import co.com.crediya.model.user.TokenProvider;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SecurityTokenProvider implements TokenProvider {
    @Override
    public Mono<String> getCurrentToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) ctx.getAuthentication())
                .map(auth -> auth.getToken().getTokenValue());
    }
}
