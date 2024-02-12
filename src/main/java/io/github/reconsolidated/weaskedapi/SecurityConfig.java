package io.github.reconsolidated.weaskedapi;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUserService;
import io.github.reconsolidated.weaskedapi.authentication.currentUser.CurrentUserArgumentResolver;
import io.github.reconsolidated.weaskedapi.googleApiService.GoogleApiService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Profile("!test")
@Configuration
@EnableWebMvc
@AllArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final AppUserService appUserService;
    private final GoogleApiService googleApiService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(new CurrentUserArgumentResolver(appUserService, googleApiService));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Twój podstawowy URI wydawcy (issuer)
        String baseIssuerUri = "accounts.google.com";

        // Stwórz dekoder JWT z wybranego źródła, na przykład z lokalizacji wydawcy OIDC
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation("https://accounts.google.com");

        // Stwórz walidator, który akceptuje obie wartości URI wydawcy
        OAuth2TokenValidator<Jwt> issuerValidator = new JwtValidator(
                jwt -> {
                    String issuer = jwt.getClaimAsString("iss");
                    return issuer != null && (issuer.equals("https://" + baseIssuerUri) || issuer.equals(baseIssuerUri));
                }
        );

        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(), // Domyślni walidatorzy (np. exp)
                issuerValidator // Twój walidator issuer
        );

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    private static class JwtValidator implements OAuth2TokenValidator<Jwt> {
        private Predicate<Jwt> predicate;

        public JwtValidator(Predicate<Jwt> predicate) {
            this.predicate = predicate;
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            return this.predicate.test(token) ?
                    OAuth2TokenValidatorResult.success() :
                    OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The ISS claim is not valid", null));
        }
    }
}
