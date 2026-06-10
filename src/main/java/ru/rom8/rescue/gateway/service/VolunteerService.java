package ru.rom8.rescue.gateway.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService {

    private static final String DEFAULT_VOLUNTEER_NAME = "волонтёр";

    public String getVolunteerName(OAuth2User oauth2User) {
        if (oauth2User == null) {
            return DEFAULT_VOLUNTEER_NAME;
        }

        String firstName = oauth2User.getAttribute("first_name");
        String lastName = oauth2User.getAttribute("last_name");
        String fullName = String.format("%s %s",
                firstName == null ? "" : firstName,
                lastName == null ? "" : lastName).trim();

        return fullName.isBlank() ? DEFAULT_VOLUNTEER_NAME : fullName;
    }
}
