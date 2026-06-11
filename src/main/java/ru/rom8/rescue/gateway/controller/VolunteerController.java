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
import ru.rom8.rescue.gateway.entity.Volunteer;
import ru.rom8.rescue.gateway.entity.VolunteerGender;
import ru.rom8.rescue.gateway.mapper.VolunteerMapper;
import ru.rom8.rescue.gateway.service.VolunteerService;
import ru.rom8.rescue.gateway.validate.VolunteerFormValidator;

@Controller
@RequestMapping("/")
public class VolunteerController {

    private static final String ROOT_PATH = "/";
    private static final String UPDATE_VOLUNTEER_PATH = "/volunteers/update";
    private static final String VOLUNTEERS_PATH = "/volunteers";
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
    private final VolunteerFormValidator volunteerFormValidator;

    public VolunteerController(VolunteerService volunteerService,
                               VolunteerMapper volunteerMapper,
                               VolunteerFormValidator volunteerFormValidator) {
        this.volunteerService = volunteerService;
        this.volunteerMapper = volunteerMapper;
        this.volunteerFormValidator = volunteerFormValidator;
    }

    @GetMapping(ROOT_PATH)
    public String volunteerHome(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        if (!volunteerService.existsByEmail(email)) {
            return redirectTo(UPDATE_VOLUNTEER_PATH);
        }

        model.addAttribute(MODEL_VOLUNTEER_NAME, volunteerService.getVolunteerName(oauth2User));
        return TEMPLATE_VOLUNTEER_HOME;
    }

    @GetMapping(UPDATE_VOLUNTEER_PATH)
    public String volunteerForm(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        var volunteer = volunteerService.getVolunteerByEmail(email);
        if (volunteer.isEmpty()) {
            return showVolunteerForm(model, VolunteerForm.withEmail(email), false);
        }

        return showVolunteerForm(model, toVolunteerForm(volunteer.get()), true);
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
            return showVolunteerForm(model, form, false);
        }

        volunteerService.addVolunteer(volunteerMapper.toVolunteer(form));
        return redirectTo(VOLUNTEER_PROFILE_PATH);
    }

    @PostMapping(UPDATE_VOLUNTEER_PATH)
    public String updateVolunteer(@AuthenticationPrincipal OAuth2User oauth2User,
                                  @ModelAttribute(MODEL_VOLUNTEER_FORM) VolunteerForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        var email = getAuthenticatedEmail(oauth2User);
        var volunteer = volunteerService.getVolunteerByEmail(email);
        if (volunteer.isEmpty()) {
            return redirectTo(UPDATE_VOLUNTEER_PATH);
        }

        form.setEmail(email);
        volunteerFormValidator.validateVolunteerForm(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return showVolunteerForm(model, form, true);
        }

        volunteerService.updateVolunteer(volunteer.get().getId(), volunteerMapper.toVolunteer(form));
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
        addVolunteerFormAttributes(model, updateMode);
        return TEMPLATE_VOLUNTEER_FORM;
    }

    private void addVolunteerFormAttributes(Model model, boolean updateMode) {
        model.addAttribute(MODEL_GENDERS, VolunteerGender.values());
        model.addAttribute(MODEL_VOLUNTEER_UPDATE_MODE, updateMode);
    }

    private VolunteerForm toVolunteerForm(Volunteer volunteer) {
        var form = new VolunteerForm();
        form.setFullName(volunteer.getFullName());
        form.setGender(volunteer.getGender());
        form.setPhoneNumber(volunteer.getPhoneNumber());
        form.setEmail(volunteer.getEmail());
        form.setBirthDate(volunteer.getBirthDate());
        form.setResidenceSettlement(volunteer.getResidenceSettlement());
        form.setResidenceDistrict(volunteer.getResidenceDistrict());
        return form;
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
