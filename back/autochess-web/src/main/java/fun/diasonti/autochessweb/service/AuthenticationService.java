package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public void attemptAuthentication(String username, String password) {
        final AppUser user = (AppUser) userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        user.eraseCredentials();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    public void attemptLogout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
