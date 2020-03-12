package fun.diasonti.autochessweb.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.diasonti.autochessweb.config.security.data.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtService {

    private final String secret;
    private final long tokenMaxAge;

    private final ObjectMapper objectMapper;
    private final JWTVerifier jwtVerifier;

    @Autowired
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.maxAge}") long tokenMaxAge,
                      ObjectMapper objectMapper) {
        this.secret = secret;
        this.tokenMaxAge = tokenMaxAge;
        this.objectMapper = objectMapper;
        this.jwtVerifier = JWT.require(HMAC512(this.secret.getBytes())).build();
    }

    public String generateToken(AppUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofSeconds(tokenMaxAge).toMillis()))
                .sign(HMAC512(secret.getBytes()));
    }

    public AppUser parseToken(String token) {
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        final String username = decodedJWT.getSubject();
        final List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        return new AppUser(username, authorities);
    }

    public long getTokenMaxAge() {
        return tokenMaxAge;
    }
}
