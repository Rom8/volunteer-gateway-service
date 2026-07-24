package ru.rom8.rescue.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rom8.rescue.gateway.api.model.VolunteerDto;
import ru.rom8.rescue.gateway.api.model.VolunteerRegisterRequest;
import ru.rom8.rescue.gateway.api.model.VolunteerUpdateRequest;
import ru.rom8.rescue.gateway.client.VolunteerClient;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerClient volunteerClient;
    private final EmailsCacheService emailsCacheService;

    public VolunteerDto createVolunteer(VolunteerRegisterRequest volunteerRegisterRequest) {
        Objects.requireNonNull(volunteerRegisterRequest, "volunteerRegisterRequest must not be null");
        UUID uuid = emailsCacheService.getUUIDOrCreateByEmail(volunteerRegisterRequest.getEmail());
        return volunteerClient.registerMe(uuid.toString(), volunteerRegisterRequest);
    }

    public VolunteerDto updateVolunteer(VolunteerUpdateRequest volunteerUpdateRequest) {
        Objects.requireNonNull(volunteerUpdateRequest, "volunteerUpdateRequest must not be null");

        UUID uuid = emailsCacheService.getUUIDOrCreateByEmail(volunteerUpdateRequest.getEmail());
        return volunteerClient.updateMe(uuid.toString(), volunteerUpdateRequest);
    }

    public Optional<VolunteerDto> getVolunteerByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        if (emailsCacheService.hasRegistered(email)) {
            UUID uuid = emailsCacheService.getUUIDOrCreateByEmail(email);
            VolunteerDto volunteerDto = volunteerClient.getMe(uuid.toString());
            return Optional.ofNullable(volunteerDto);
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return emailsCacheService.hasRegistered(email);
    }
}
