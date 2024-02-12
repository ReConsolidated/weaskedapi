package io.github.reconsolidated.weaskedapi.authentication.appUser;

import io.github.reconsolidated.weaskedapi.SecurityConfig;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register() {
        return ResponseEntity.ok(appUserService.register());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@CurrentUser AppUser user) {
        appUserService.deleteUser(user);
        return ResponseEntity.ok().build();
    }
}
