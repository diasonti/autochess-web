package fun.diasonti.autochessweb.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/secured")
public class SecurityTestController {

    @GetMapping("/whoami")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> whoami() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(Collections.singletonMap("username", user.getUsername()));
    }

    @GetMapping("/nogo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> nogo() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(Collections.singletonMap("username", user.getUsername()));
    }

}
