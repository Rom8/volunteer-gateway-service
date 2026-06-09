package ru.rom8.rescue.volunteer_gateway_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String LOGIN_PAGE = "/login.html";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                LOGIN_PAGE,
                                "/css/**",
                                "/fonts/**",
                                "/images/**",
                                "/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.loginPage(LOGIN_PAGE))
                .build();
    }
}
