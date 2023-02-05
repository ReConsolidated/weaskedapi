package io.github.reconsolidated.weaskedapi.authentication.Security.config;

import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/registration/**")
                .permitAll()
                .antMatchers("/registration")
                .permitAll()
                .antMatchers("/comments/**")
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