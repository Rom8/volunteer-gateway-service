package ru.rom8.rescue.gateway.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import ru.rom8.rescue.gateway.entity.VolunteerGender;

public class VolunteerForm {

    private String fullName;
    private VolunteerGender gender;
    private String phoneNumber;
    private String email;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    private String residenceSettlement;
    private String residenceDistrict;

    public VolunteerForm() {
    }

    public static VolunteerForm withEmail(String email) {
        var form = new VolunteerForm();
        form.setEmail(email);
        return form;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public VolunteerGender getGender() {
        return gender;
    }

    public void setGender(VolunteerGender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getResidenceSettlement() {
        return residenceSettlement;
    }

    public void setResidenceSettlement(String residenceSettlement) {
        this.residenceSettlement = residenceSettlement;
    }

    public String getResidenceDistrict() {
        return residenceDistrict;
    }

    public void setResidenceDistrict(String residenceDistrict) {
        this.residenceDistrict = residenceDistrict;
    }
}
