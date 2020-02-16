package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.jwt.JwtAuthenticationFilter;
import fun.diasonti.autochessweb.config.security.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        try {
            final String token = jwtService.generateToken(username, password);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            log.debug("Failed authentication attempt username=\"{}\", pass=\"{}\"", username, password);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> login(@RequestHeader(JwtAuthenticationFilter.HEADER_NAME) String header) {
        try {
            final String token = jwtService.generateToken(header.replace(JwtAuthenticationFilter.TOKEN_PREFIX, ""));
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            log.debug("Failed refresh attempt token=\"{}\"", header);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
