package io.github.reconsolidated.weaskedapi.authentication.currentUser;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUserService;
import io.github.reconsolidated.weaskedapi.googleApiService.GoogleApiService;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@AllArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final AppUserService appUserService;
    private final GoogleApiService googleApiService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null || !(authentication.getCredentials() instanceof Jwt authToken)) {
            throw new BadCredentialsException("Authentication required");
        }
        Optional<AppUser> appUser = appUserService.getByEmail(authToken.getClaims().get("email").toString());
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User with email %s not found. Register first."
                    .formatted(authToken.getClaims().get("email").toString()));
        }
        googleApiService.enrichUserInfo(appUser.get(), authToken.getTokenValue());
        return appUser.get();
    }

}
