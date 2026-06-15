package ru.rom8.rescue.gateway.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.rom8.rescue.gateway.entity.VolunteerGender;

@Setter
@Getter
public class VolunteerForm {

    private String fullName;
    private String familyName;
    private String firstName;
    private String patronymic;
    private VolunteerGender gender;
    private String phoneNumber;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    private String residenceSettlement;
    private String residenceDistrict;

    public static VolunteerForm withEmail(String email) {
        var form = new VolunteerForm();
        form.setEmail(email);
        return form;
    }
}
