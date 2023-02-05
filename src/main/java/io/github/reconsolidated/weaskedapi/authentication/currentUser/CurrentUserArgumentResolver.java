package io.github.reconsolidated.weaskedapi.authentication.currentUser;

import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new BadCredentialsException("Authentication required");
        }
        if (!(authentication.getPrincipal() instanceof AppUser user)) {
            throw new BadCredentialsException("User not found");
        }
        if (user.getId() == null) {
            throw new BadCredentialsException("User id not found");
        }
        return user;
    }

}
