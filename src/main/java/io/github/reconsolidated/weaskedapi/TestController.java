package io.github.reconsolidated.weaskedapi;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final GoogleAccountParser googleAccountParser;

    @GetMapping("/api/public/hello")
    public String publicEndpoint() {
        return "Hello, this is a public endpoint!";
    }

    @PostMapping("/secured")
    public String securedEndpoint(@CurrentUser AppUser currentUser, @RequestParam String token) {
        System.out.println("Current user: " + currentUser.getEmail());
        return "Hello, this is a secured endpoint!";
    }
}
