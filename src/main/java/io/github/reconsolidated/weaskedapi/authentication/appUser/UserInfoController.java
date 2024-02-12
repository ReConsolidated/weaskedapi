package io.github.reconsolidated.weaskedapi.authentication.appUser;

import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserInfoController {

    @GetMapping("/current")
    @ResponseBody
    public AppUser currentUserInfo(@CurrentUser AppUser currentUser) {
        String uri = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
        return currentUser;
    }

    public HttpEntity<?> createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Ustawienie nagłówka autoryzacji z tokenem dostępu
        return new HttpEntity<>(headers);
    }
}


