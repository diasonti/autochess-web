package fun.diasonti.autochessweb.controller.api;

import com.google.common.collect.ImmutableMap;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.ValidationErrors;
import fun.diasonti.autochessweb.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final UserAccountService userAccountService;

    @Autowired
    public RegistrationController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/register")
    public ResponseEntity<ValidationErrors> registerAccount(UserAccountForm form) {
        userAccountService.cleanForm(form);
        final ValidationErrors errors = userAccountService.validateForm(form);
        if (!errors.hasErrors()) {
            userAccountService.createNewUser(form);
        }
        return ResponseEntity.ok(errors);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Boolean>> confirmEmail(@RequestParam String email, @RequestParam String token) {
        final boolean emailConfirmed = userAccountService.attemptEmailConfirmation(email, token);
        return ResponseEntity.ok(ImmutableMap.of("emailConfirmed", emailConfirmed));
    }
}
