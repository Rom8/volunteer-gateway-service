package ru.rom8.rescue.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rom8.rescue.gateway.entity.Volunteer;
import ru.rom8.rescue.gateway.repository.VolunteerRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Transactional
    public Volunteer addVolunteer(Volunteer volunteer) {
        Objects.requireNonNull(volunteer, "volunteer must not be null");
        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Volunteer updateVolunteer(Long id, Volunteer updatedVolunteer) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(updatedVolunteer, "updatedVolunteer must not be null");

        return volunteerRepository.save(getVolunteer(id).updateFrom(updatedVolunteer));
    }

    @Transactional(readOnly = true)
    public Volunteer getVolunteer(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found by id: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Volunteer> getVolunteerByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return volunteerRepository.findByEmailIgnoreCase(email.trim());
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return email != null && !email.isBlank() && volunteerRepository.existsByEmailIgnoreCase(email.trim());
    }
}
