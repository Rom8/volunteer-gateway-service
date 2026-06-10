package ru.rom8.rescue.volunteer_gateway_service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SimpleController {

    private static final String VOLUNTEER_HOME_VIEW = "volunteer-home";
    private static final String VOLUNTEER_NAME_ATTRIBUTE = "volunteerName";
    private static final String FIRST_NAME_ATTRIBUTE = "first_name";
    private static final String LAST_NAME_ATTRIBUTE = "last_name";
    private static final String DEFAULT_VOLUNTEER_NAME = "волонтёр";

    @GetMapping("/")
    public String volunteerHome(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        model.addAttribute(VOLUNTEER_NAME_ATTRIBUTE, getVolunteerName(oauth2User));
        return VOLUNTEER_HOME_VIEW;
    }

    private String getVolunteerName(OAuth2User oauth2User) {
        if (oauth2User == null) {
            return DEFAULT_VOLUNTEER_NAME;
        }

        String firstName = oauth2User.getAttribute(FIRST_NAME_ATTRIBUTE);
        String lastName = oauth2User.getAttribute(LAST_NAME_ATTRIBUTE);
        String fullName = String.format("%s %s",
                firstName == null ? "" : firstName,
                lastName == null ? "" : lastName).trim();

        return fullName.isBlank() ? DEFAULT_VOLUNTEER_NAME : fullName;
    }
}
