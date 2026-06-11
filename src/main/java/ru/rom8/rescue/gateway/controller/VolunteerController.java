package ru.rom8.rescue.gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rom8.rescue.gateway.service.VolunteerService;

@Controller
@RequestMapping("/")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/")
    public String volunteerHome(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        model.addAttribute("volunteerName", volunteerService.getVolunteerName(oauth2User));
        return "volunteer-home";
    }
}
