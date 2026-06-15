package ru.rom8.rescue.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rom8.rescue.gateway.dto.VolunteerForm;
import ru.rom8.rescue.gateway.entity.Volunteer;
import ru.rom8.rescue.gateway.entity.VolunteerGender;
import ru.rom8.rescue.gateway.mapper.VolunteerMapper;
import ru.rom8.rescue.gateway.service.VolunteerService;

import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class VolunteerController {

    private static final String ROOT_PATH = "/";
    private static final String UPDATE_VOLUNTEER_PATH = "/volunteers/update";
    private static final String VOLUNTEER_PROFILE_PATH = "/volunteers/me";
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String TEMPLATE_VOLUNTEER_HOME = "volunteer-home";
    private static final String TEMPLATE_VOLUNTEER_FORM = "volunteer-form";
    private static final String TEMPLATE_VOLUNTEER_DETAILS = "volunteer-details";
    private static final String MODEL_VOLUNTEER = "volunteer";
    private static final String MODEL_VOLUNTEER_FORM = "volunteerForm";
    private static final String MODEL_VOLUNTEER_NAME = "volunteerName";
    private static final String MODEL_VOLUNTEER_UPDATE_MODE = "volunteerUpdateMode";
    private static final String MODEL_GENDERS = "genders";
    private static final String MISSING_EMAIL_MESSAGE = "OAuth2 user email is missing";

    private final VolunteerService volunteerService;
    private final VolunteerMapper volunteerMapper;

    @GetMapping(ROOT_PATH)
    public String volunteerHome(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        Optional<Volunteer> optionalVolunteer = volunteerService.getVolunteerByEmail(email);
        if (optionalVolunteer.isEmpty()) {
            return redirectTo(UPDATE_VOLUNTEER_PATH);
        }

        Volunteer volunteer = optionalVolunteer.get();
        model.addAttribute(MODEL_VOLUNTEER_NAME, volunteer.getFirstName() + " " + volunteer.getFamilyName());
        return TEMPLATE_VOLUNTEER_HOME;
    }

    @GetMapping(UPDATE_VOLUNTEER_PATH)
    public String volunteerForm(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        Optional<Volunteer> volunteerOpt = volunteerService.getVolunteerByEmail(email);
        if (volunteerOpt.isEmpty()) {
            return showVolunteerForm(model, VolunteerForm.withEmail(email), false);
        }

        return showVolunteerForm(model, volunteerMapper.toVolunteerForm(volunteerOpt.get()), true);
    }

    @PostMapping(UPDATE_VOLUNTEER_PATH)
    public String updateVolunteer(@AuthenticationPrincipal OAuth2User oauth2User,
                                  @Validated @ModelAttribute(MODEL_VOLUNTEER_FORM) VolunteerForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        final String email = getAuthenticatedEmail(oauth2User);
        form.setEmail(email);
        if (bindingResult.hasErrors()) {
            return showVolunteerForm(model, form, true);
        }

        Optional<Volunteer> volunteerOpt = volunteerService.getVolunteerByEmail(email);
        if (volunteerOpt.isEmpty()) {
            volunteerService.addVolunteer(volunteerMapper.toVolunteer(form));
        } else {
            volunteerService.updateVolunteer(volunteerOpt.get().getId(), volunteerMapper.toVolunteer(form));
        }
        return redirectTo(VOLUNTEER_PROFILE_PATH);
    }

    @GetMapping(VOLUNTEER_PROFILE_PATH)
    public String volunteerDetails(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        var volunteer = volunteerService.getVolunteerByEmail(email);
        if (volunteer.isEmpty()) {
            return redirectTo(UPDATE_VOLUNTEER_PATH);
        }

        model.addAttribute(MODEL_VOLUNTEER, volunteer.get());
        return TEMPLATE_VOLUNTEER_DETAILS;
    }

    private String showVolunteerForm(Model model, VolunteerForm form, boolean updateMode) {
        model.addAttribute(MODEL_VOLUNTEER_FORM, form);
        model.addAttribute(MODEL_GENDERS, VolunteerGender.values());
        model.addAttribute(MODEL_VOLUNTEER_UPDATE_MODE, updateMode);
        return TEMPLATE_VOLUNTEER_FORM;
    }

    private String getAuthenticatedEmail(OAuth2User oauth2User) {
        var email = oauth2User == null ? null : oauth2User.getName();
        if (email == null || email.isBlank()) {
            throw new IllegalStateException(MISSING_EMAIL_MESSAGE);
        }
        return email.trim();
    }

    private String redirectTo(String path) {
        return REDIRECT_PREFIX + path;
    }
}
