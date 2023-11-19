package io.github.reconsolidated.weaskedapi.authentication.appUser;

import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserInfoController {

    @GetMapping("/current")
    @ResponseBody
    public AppUser currentUserInfo(@CurrentUser AppUser currentUser) {
        return currentUser;
    }
}
