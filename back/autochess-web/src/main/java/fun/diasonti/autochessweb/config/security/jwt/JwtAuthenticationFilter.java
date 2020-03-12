package fun.diasonti.autochessweb.config.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import fun.diasonti.autochessweb.config.security.data.AppUser;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public static final String COOKIE_NAME = "session";

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        final Cookie[] cookies = ArrayUtils.nullToEmpty(request.getCookies(), Cookie[].class);
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                token = cookie.getValue();
                break;
            }
        }
        boolean wasAuthenticatedBefore = false;
        if (StringUtils.isNotBlank(token)) {
            final Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            wasAuthenticatedBefore = authentication != null;
        }

        filterChain.doFilter(request, response);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (wasAuthenticatedBefore) {
            if (authentication == null || !(authentication.getPrincipal() instanceof AppUser)) {
                response.addCookie(createSessionCookie(StringUtils.EMPTY, 0));
            }
        } else  {
            if (authentication != null && authentication.getPrincipal() instanceof AppUser) {
                final AppUser user = (AppUser) authentication.getPrincipal();
                token = jwtService.generateToken(user);
                response.addCookie(createSessionCookie(token, (int) jwtService.getTokenMaxAge()));
            }
        }
    }

    private Cookie createSessionCookie(String token, int maxAge) {
        final Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    private Authentication getAuthentication(String token) {
        Authentication authentication = null;
        AppUser user = null;
        try {
             user = jwtService.parseToken(token);
        } catch (JWTVerificationException e) {
            log.debug("getAuthentication error with token '{}'", token, e);
        }
        if (user != null) {
            authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        return authentication;
    }
}
