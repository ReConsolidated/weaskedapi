package io.github.reconsolidated.weaskedapi.authentication.Registration;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/registration")
@AllArgsConstructor
public class UserRegistrationController {

    private RegistrationService registrationService;


    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
