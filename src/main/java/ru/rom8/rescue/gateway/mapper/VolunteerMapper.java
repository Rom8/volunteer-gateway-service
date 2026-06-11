package ru.rom8.rescue.gateway.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.rom8.rescue.gateway.dto.VolunteerForm;
import ru.rom8.rescue.gateway.entity.Volunteer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VolunteerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", source = "fullName", qualifiedByName = "trim")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "trim")
    @Mapping(target = "email", source = "email", qualifiedByName = "trim")
    @Mapping(target = "residenceSettlement", source = "residenceSettlement", qualifiedByName = "trim")
    @Mapping(target = "residenceDistrict", source = "residenceDistrict", qualifiedByName = "trimToNull")
    Volunteer toVolunteer(VolunteerForm form);

    @Named("trim")
    default String trim(String value) {
        return value == null ? null : value.trim();
    }

    @Named("trimToNull")
    default String trimToNull(String value) {
        var trimmedValue = trim(value);
        return trimmedValue == null || trimmedValue.isBlank() ? null : trimmedValue;
    }
}
