package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.controller.exceptions.UnauthorizedException;
import fun.diasonti.autochessweb.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password) {
        try {
            authenticationService.attemptAuthentication(username, password);
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
    }

    @PostMapping("/logout")
    public void logout() {
        authenticationService.attemptLogout();
    }
}
