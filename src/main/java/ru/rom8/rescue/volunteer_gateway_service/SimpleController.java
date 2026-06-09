package ru.rom8.rescue.volunteer_gateway_service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/")
public class SimpleController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    public String hello(@AuthenticationPrincipal OAuth2User oauth2User) {
        return String.format("Привет, %s! (%s)", oauth2User.getName(), counter.incrementAndGet());
    }
}
