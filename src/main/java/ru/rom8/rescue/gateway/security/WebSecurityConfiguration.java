package ru.rom8.rescue.gateway.security;

import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import ru.rom8.rescue.gateway.service.VolunteerService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final String LOGIN_PAGE = "/login.html";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_PAGE + "?logout";
    private static final String VOLUNTEER_ROLE = "ROLE_VOLUNTEER";
    private static final GrantedAuthority VOLUNTEER_AUTHORITY = new SimpleGrantedAuthority(VOLUNTEER_ROLE);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService) {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                LOGIN_PAGE,
                                "/css/**",
                                "/fonts/**",
                                "/images/**",
                                "/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage(LOGIN_PAGE)
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService)))
                .logout(logout -> logout
                        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                        .permitAll())
                .build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(VolunteerService volunteerService) {
        var delegate = new DefaultOAuth2UserService();
        return userRequest -> addVolunteerRole(delegate.loadUser(userRequest), userRequest, volunteerService);
    }

    private OAuth2User addVolunteerRole(OAuth2User oauth2User,
                                        OAuth2UserRequest userRequest,
                                        VolunteerService volunteerService) {
        if (!volunteerService.existsByEmail(oauth2User.getName())) {
            return oauth2User;
        }

        var authorities = new HashSet<GrantedAuthority>(oauth2User.getAuthorities());
        authorities.add(VOLUNTEER_AUTHORITY);

        var userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        if (userNameAttributeName == null || userNameAttributeName.isBlank()) {
            throw new OAuth2AuthenticationException("Missing OAuth2 user name attribute");
        }

        return new DefaultOAuth2User(authorities, oauth2User.getAttributes(), userNameAttributeName);
    }
}
