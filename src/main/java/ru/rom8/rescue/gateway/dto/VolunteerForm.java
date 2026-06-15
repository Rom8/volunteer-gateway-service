package ru.rom8.rescue.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rom8.rescue.gateway.entity.VolunteerGender;

@Setter
@Getter
public class VolunteerForm {

    @NotBlank
    private String familyName;
    @NotBlank
    private String firstName;
    private String patronymic;
    @NotNull
    private VolunteerGender gender;
    @NotBlank(message = "{required.volunteerForm.phoneNumber}")
    private String phoneNumber;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate birthDate;
    @NotBlank(message = "{required.volunteerForm.residenceSettlement}")
    private String residenceSettlement;
    private String residenceDistrict;

    public static VolunteerForm withEmail(String email) {
        var form = new VolunteerForm();
        form.setEmail(email);
        return form;
    }
}
