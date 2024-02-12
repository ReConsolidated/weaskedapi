package io.github.reconsolidated.weaskedapi.authentication.appUser;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public Optional<AppUser> getById(long id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> getByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }

    public AppUser register() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null || !(authentication.getCredentials() instanceof Jwt authToken)) {
            throw new BadCredentialsException("Authentication required");
        }
        Optional<AppUser> appUser = getByEmail(authToken.getClaims().get("email").toString());
        if (appUser.isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            AppUser user = new AppUser();
            user.setRole(AppUserRole.USER);
            user.setNickname(authToken.getClaims().get("name").toString());
            user.setImageUrl(authToken.getClaims().get("picture").toString());
            user.setEmail(authToken.getClaims().get("email").toString());
            return appUserRepository.save(user);
        }
    }
}
