package ru.rom8.rescue.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rom8.rescue.gateway.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
