package ru.rom8.rescue.gateway.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.rom8.rescue.gateway.api.model.VolunteerDto;
import ru.rom8.rescue.gateway.api.model.VolunteerRegisterRequest;
import ru.rom8.rescue.gateway.api.model.VolunteerUpdateRequest;
import ru.rom8.rescue.gateway.dto.VolunteerForm;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VolunteerMapper {

    @Mapping(target = "familyName", source = "familyName", qualifiedByName = "trim")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "trim")
    @Mapping(target = "patronymic", source = "patronymic", qualifiedByName = "trim")
    @Mapping(target = "phoneNumber", source = "contacts", qualifiedByName = "phoneNumber")
    @Mapping(target = "email", source = "contacts", qualifiedByName = "email")
    @Mapping(target = "residenceSettlement", source = "settlementName")
    @Mapping(target = "residenceDistrict", source = "settlementDistrictName")
    VolunteerForm toVolunteerForm(VolunteerDto volunteerDto);

    @Mapping(target = "familyName", source = "familyName", qualifiedByName = "trim")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "trim")
    @Mapping(target = "patronymic", source = "patronymic", qualifiedByName = "trim")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "trim")
    @Mapping(target = "email", source = "email", qualifiedByName = "trim")
    @Mapping(target = "settlementName", source = "residenceSettlement", qualifiedByName = "trim")
    @Mapping(target = "settlementDistrictName", source = "residenceDistrict", qualifiedByName = "trimToNull")
    VolunteerUpdateRequest toVolunteerUpdateRequest(VolunteerForm form);

    @Mapping(target = "familyName", source = "familyName", qualifiedByName = "trim")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "trim")
    @Mapping(target = "patronymic", source = "patronymic", qualifiedByName = "trim")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "trim")
    @Mapping(target = "email", source = "email", qualifiedByName = "trim")
    @Mapping(target = "settlementName", source = "residenceSettlement", qualifiedByName = "trim")
    @Mapping(target = "settlementDistrictName", source = "residenceDistrict", qualifiedByName = "trimToNull")
    VolunteerRegisterRequest toVolunteerRegisterRequest(VolunteerForm form);

    @Named("trim")
    default String trim(String value) {
        return value == null ? null : value.trim();
    }

    @Named("trimToNull")
    default String trimToNull(String value) {
        var trimmedValue = trim(value);
        return trimmedValue == null || trimmedValue.isBlank() ? null : trimmedValue;
    }

    @Named("phoneNumber")
    default String phoneNumber(java.util.Set<ru.rom8.rescue.gateway.api.model.ContactInfoDto> contacts) {
        return findContact(contacts, ru.rom8.rescue.gateway.api.model.ContactType.PHONE);
    }

    @Named("email")
    default String email(java.util.Set<ru.rom8.rescue.gateway.api.model.ContactInfoDto> contacts) {
        return findContact(contacts, ru.rom8.rescue.gateway.api.model.ContactType.EMAIL);
    }

    private String findContact(java.util.Set<ru.rom8.rescue.gateway.api.model.ContactInfoDto> contacts,
                               ru.rom8.rescue.gateway.api.model.ContactType contactType) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream()
                .filter(contact -> contactType.equals(contact.getContactType()))
                .map(ru.rom8.rescue.gateway.api.model.ContactInfoDto::getContact)
                .findFirst()
                .map(this::trim)
                .orElse(null);
    }
}
