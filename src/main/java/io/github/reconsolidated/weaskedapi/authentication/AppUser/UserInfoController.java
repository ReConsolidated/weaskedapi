package io.github.reconsolidated.weaskedapi.authentication.AppUser;

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
    public AppUser currentUserInfo() {
        return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
