package ru.rom8.rescue.volunteer_gateway_service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SimpleController {

    @GetMapping("/")
    public String hello(@AuthenticationPrincipal OAuth2User oauth2User) {
        String name = String.format("%s %s",
                oauth2User.getAttribute("first_name"),
                oauth2User.getAttribute("last_name"));
        return String.format("Привет, %s!", name);
    }
}
