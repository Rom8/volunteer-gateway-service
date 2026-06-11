package ru.rom8.rescue.gateway.validate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.rom8.rescue.gateway.dto.VolunteerForm;

@Service
public class VolunteerFormValidator {

    private static final String FIELD_FULL_NAME = "fullName";
    private static final String FIELD_GENDER = "gender";
    private static final String FIELD_PHONE_NUMBER = "phoneNumber";
    private static final String FIELD_BIRTH_DATE = "birthDate";
    private static final String FIELD_RESIDENCE_SETTLEMENT = "residenceSettlement";
    private static final String VALIDATION_CODE_REQUIRED = "required";
    private static final String REQUIRED_FIELD_MESSAGE = "Заполните обязательное поле";

    public void validateVolunteerForm(VolunteerForm form, BindingResult bindingResult) {
        rejectIfBlank(bindingResult, FIELD_FULL_NAME, form.getFullName());
        rejectIfBlank(bindingResult, FIELD_PHONE_NUMBER, form.getPhoneNumber());
        rejectIfBlank(bindingResult, FIELD_RESIDENCE_SETTLEMENT, form.getResidenceSettlement());
        rejectIfMissing(bindingResult, FIELD_GENDER, form.getGender());
        rejectIfMissing(bindingResult, FIELD_BIRTH_DATE, form.getBirthDate());
    }

    private void rejectIfBlank(BindingResult bindingResult, String fieldName, String value) {
        if ((value == null || value.isBlank()) && !bindingResult.hasFieldErrors(fieldName)) {
            bindingResult.rejectValue(fieldName, VALIDATION_CODE_REQUIRED, REQUIRED_FIELD_MESSAGE);
        }
    }

    private void rejectIfMissing(BindingResult bindingResult, String fieldName, Object value) {
        if (value == null && !bindingResult.hasFieldErrors(fieldName)) {
            bindingResult.rejectValue(fieldName, VALIDATION_CODE_REQUIRED, REQUIRED_FIELD_MESSAGE);
        }
    }

}
