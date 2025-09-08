package co.com.crediya.security.helper;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtRoleConverterTest {
    private final JwtRoleConverter converter = new JwtRoleConverter();

    @Test
    void testConvertWithRole() {
        Map<String, Object> claims = Map.of("role", "admin");
        Map<String, Object> headers = Map.of("alg", "RS256");
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);


        Collection<GrantedAuthority> authorities = converter.convert(jwt);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testConvertWithoutRole() {
        Map<String, Object> headers = Map.of("alg", "RS256");
        Map<String, Object> claims = Map.of("sub", "test");

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);

        Collection<GrantedAuthority> authorities = converter.convert(jwt);
        assertTrue(authorities.isEmpty());
    }
}