package ru.rom8.rescue.gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rom8.rescue.gateway.entity.Volunteer;
import ru.rom8.rescue.gateway.repository.VolunteerRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Transactional
    public Volunteer addVolunteer(Volunteer volunteer) {
        Objects.requireNonNull(volunteer, "volunteer must not be null");
        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Volunteer updateVolunteer(Long id, Volunteer updatedVolunteer) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(updatedVolunteer, "updatedVolunteer must not be null");

        var volunteer = getVolunteer(id);
        volunteer.setFullName(updatedVolunteer.getFullName());
        volunteer.setGender(updatedVolunteer.getGender());
        volunteer.setPhoneNumber(updatedVolunteer.getPhoneNumber());
        volunteer.setEmail(updatedVolunteer.getEmail());
        volunteer.setBirthDate(updatedVolunteer.getBirthDate());
        volunteer.setResidenceSettlement(updatedVolunteer.getResidenceSettlement());
        volunteer.setResidenceDistrict(updatedVolunteer.getResidenceDistrict());
        return volunteerRepository.save(volunteer);
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
