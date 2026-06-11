package ru.rom8.rescue.gateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rom8.rescue.gateway.dto.VolunteerForm;
import ru.rom8.rescue.gateway.entity.VolunteerGender;
import ru.rom8.rescue.gateway.mapper.VolunteerMapper;
import ru.rom8.rescue.gateway.service.VolunteerService;
import ru.rom8.rescue.gateway.validate.VolunteerFormValidator;

@Controller
@RequestMapping("/")
public class VolunteerController {

    private static final String ROOT_PATH = "/";
    private static final String NEW_VOLUNTEER_PATH = "/volunteers/new";
    private static final String VOLUNTEERS_PATH = "/volunteers";
    private static final String VOLUNTEER_PROFILE_PATH = "/volunteers/me";
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String TEMPLATE_VOLUNTEER_HOME = "volunteer-home";
    private static final String TEMPLATE_VOLUNTEER_FORM = "volunteer-form";
    private static final String TEMPLATE_VOLUNTEER_DETAILS = "volunteer-details";
    private static final String MODEL_VOLUNTEER = "volunteer";
    private static final String MODEL_VOLUNTEER_FORM = "volunteerForm";
    private static final String MODEL_VOLUNTEER_NAME = "volunteerName";
    private static final String MODEL_GENDERS = "genders";
    private static final String MISSING_EMAIL_MESSAGE = "OAuth2 user email is missing";

    private final VolunteerService volunteerService;
    private final VolunteerMapper volunteerMapper;
    private final VolunteerFormValidator volunteerFormValidator;

    public VolunteerController(VolunteerService volunteerService, VolunteerMapper volunteerMapper, VolunteerFormValidator volunteerFormValidator) {
        this.volunteerService = volunteerService;
        this.volunteerMapper = volunteerMapper;
        this.volunteerFormValidator = volunteerFormValidator;
    }

    @GetMapping(ROOT_PATH)
    public String volunteerHome(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        if (!volunteerService.existsByEmail(email)) {
            return redirectTo(NEW_VOLUNTEER_PATH);
        }

        model.addAttribute(MODEL_VOLUNTEER_NAME, volunteerService.getVolunteerName(oauth2User));
        return TEMPLATE_VOLUNTEER_HOME;
    }

    @GetMapping(NEW_VOLUNTEER_PATH)
    public String newVolunteer(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        if (volunteerService.existsByEmail(email)) {
            return redirectTo(VOLUNTEER_PROFILE_PATH);
        }

        model.addAttribute(MODEL_VOLUNTEER_FORM, VolunteerForm.withEmail(email));
        addVolunteerFormAttributes(model);
        return TEMPLATE_VOLUNTEER_FORM;
    }

    @PostMapping(VOLUNTEERS_PATH)
    public String createVolunteer(@AuthenticationPrincipal OAuth2User oauth2User,
                                  @ModelAttribute(MODEL_VOLUNTEER_FORM) VolunteerForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        if (volunteerService.existsByEmail(email)) {
            return redirectTo(VOLUNTEER_PROFILE_PATH);
        }

        form.setEmail(email);
        volunteerFormValidator.validateVolunteerForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            addVolunteerFormAttributes(model);
            return TEMPLATE_VOLUNTEER_FORM;
        }

        volunteerService.addVolunteer(volunteerMapper.toVolunteer(form));
        return redirectTo(VOLUNTEER_PROFILE_PATH);
    }

    @GetMapping(VOLUNTEER_PROFILE_PATH)
    public String volunteerDetails(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        var volunteer = volunteerService.getVolunteerByEmail(email);
        if (volunteer.isEmpty()) {
            return redirectTo(NEW_VOLUNTEER_PATH);
        }

        model.addAttribute(MODEL_VOLUNTEER, volunteer.get());
        return TEMPLATE_VOLUNTEER_DETAILS;
    }

    private void addVolunteerFormAttributes(Model model) {
        model.addAttribute(MODEL_GENDERS, VolunteerGender.values());
    }

    private String getAuthenticatedEmail(OAuth2User oauth2User) {
        if (oauth2User == null || oauth2User.getName().isBlank()) {
            throw new IllegalStateException(MISSING_EMAIL_MESSAGE);
        }
        return oauth2User.getName().trim();
    }

    private String redirectTo(String path) {
        return REDIRECT_PREFIX + path;
    }
}
