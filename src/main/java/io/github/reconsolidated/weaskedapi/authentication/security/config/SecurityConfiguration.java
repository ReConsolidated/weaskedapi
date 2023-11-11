package io.github.reconsolidated.weaskedapi.authentication.security.config;

import io.github.reconsolidated.weaskedapi.authentication.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**")
                .permitAll()
                .antMatchers("/swagger-ui.html")
                .permitAll()
                .antMatchers("/v3/api-docs/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/comments/**")
                .permitAll()
                .antMatchers("/clear_all")
                .permitAll()
                .antMatchers("/test")
                .permitAll()
                .antMatchers("/registration/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

}