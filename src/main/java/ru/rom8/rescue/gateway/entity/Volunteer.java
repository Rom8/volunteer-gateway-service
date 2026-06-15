package ru.rom8.rescue.gateway.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Волонтёр, зарегистрированный в системе спасательного проекта.
 *
 * <p>Запись этой сущности используется для хранения персональных данных волонтёра
 * и для принятия решения о выдаче роли {@code ROLE_VOLUNTEER} при OAuth2-входе.</p>
 */
@Entity
@Table(name = "volunteer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer {

    /** Уникальный идентификатор волонтёра. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Фамилия волонтёра. */
    @Column(name = "family_name", nullable = false)
    private String familyName;

    /** Имя волонтёра. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Отчество волонтёра. */
    @Column(name = "patronymic")
    private String patronymic;

    /** Пол волонтёра. */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private VolunteerGender gender;

    /** Контактный номер телефона волонтёра. */
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    /** Email волонтёра, используемый для сопоставления с OAuth2-пользователем. */
    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    /** Дата рождения волонтёра. */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /** Название населённого пункта проживания волонтёра. */
    @Column(name = "residence_settlement", nullable = false)
    private String residenceSettlement;

    /** Район населённого пункта проживания волонтёра. */
    @Column(name = "residence_district")
    private String residenceDistrict;

    public Volunteer updateFrom(Volunteer updatedVolunteer) {
        setFamilyName(updatedVolunteer.getFamilyName());
        setFirstName(updatedVolunteer.getFirstName());
        setPatronymic(updatedVolunteer.getPatronymic());
        setGender(updatedVolunteer.getGender());
        setPhoneNumber(updatedVolunteer.getPhoneNumber());
        setEmail(updatedVolunteer.getEmail());
        setBirthDate(updatedVolunteer.getBirthDate());
        setResidenceSettlement(updatedVolunteer.getResidenceSettlement());
        setResidenceDistrict(updatedVolunteer.getResidenceDistrict());
        return this;
    }
}
