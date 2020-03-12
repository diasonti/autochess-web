package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.controller.exceptions.UnauthorizedException;
import fun.diasonti.autochessweb.data.form.PlayerForm;
import fun.diasonti.autochessweb.service.AuthenticationService;
import fun.diasonti.autochessweb.service.PlayerFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PlayerFormService playerFormService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, PlayerFormService playerFormService) {
        this.authenticationService = authenticationService;
        this.playerFormService = playerFormService;
    }

    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password) {
        try {
            authenticationService.attemptAuthentication(username, password);
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
    }

    @GetMapping("/fetch")
    public PlayerForm fetch(AppUser user) {
        return playerFormService.getByUsername(user.getUsername());
    }

    @PostMapping("/logout")
    public void logout() {
        authenticationService.attemptLogout();
    }
}
