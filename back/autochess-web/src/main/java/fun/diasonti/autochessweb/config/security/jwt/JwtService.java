package fun.diasonti.autochessweb.config.security.jwt;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JwtService {

    @Value("${jwt.secret:secret}")
    private String secret;    //retrieve username from jwt token

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JwtService(UserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(String username, String password) {
        final UserDetails user = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofDays(10).toMillis()))
                .sign(HMAC512(secret.getBytes()));
    }

    public String generateToken(String oldToken) {
        final String username = verifyToken(oldToken);
        final UserDetails user = userDetailsService.loadUserByUsername(username);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofDays(10).toMillis()))
                .sign(HMAC512(secret.getBytes()));
    }

    public String verifyToken(String token) {
        return JWT.require(HMAC512(secret.getBytes())).build()
                .verify(token)
                .getSubject();
    }

}
