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

/**
 * Волонтёр, зарегистрированный в системе спасательного проекта.
 *
 * <p>Запись этой сущности используется для хранения персональных данных волонтёра
 * и для принятия решения о выдаче роли {@code ROLE_VOLUNTEER} при OAuth2-входе.</p>
 */
@Entity
@Table(name = "volunteer")
public class Volunteer {

    /** Уникальный идентификатор волонтёра. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Фамилия, имя и отчество волонтёра. */
    @Column(name = "full_name", nullable = false)
    private String fullName;

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

    /**
     * Конструктор для JPA.
     */
    protected Volunteer() {
    }

    /**
     * Создаёт нового волонтёра.
     *
     * @param fullName фамилия, имя и отчество
     * @param gender пол
     * @param phoneNumber номер телефона
     * @param email email для связи и сопоставления с OAuth2-пользователем
     * @param birthDate дата рождения
     * @param residenceSettlement населённый пункт проживания
     * @param residenceDistrict район населённого пункта проживания
     */
    public Volunteer(String fullName,
                     VolunteerGender gender,
                     String phoneNumber,
                     String email,
                     LocalDate birthDate,
                     String residenceSettlement,
                     String residenceDistrict) {
        this.fullName = fullName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.residenceSettlement = residenceSettlement;
        this.residenceDistrict = residenceDistrict;
    }

    /**
     * Возвращает уникальный идентификатор волонтёра.
     *
     * @return идентификатор волонтёра
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает ФИО волонтёра.
     *
     * @return фамилия, имя и отчество
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Устанавливает ФИО волонтёра.
     *
     * @param fullName фамилия, имя и отчество
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Возвращает пол волонтёра.
     *
     * @return пол волонтёра
     */
    public VolunteerGender getGender() {
        return gender;
    }

    /**
     * Устанавливает пол волонтёра.
     *
     * @param gender пол волонтёра
     */
    public void setGender(VolunteerGender gender) {
        this.gender = gender;
    }

    /**
     * Возвращает номер телефона волонтёра.
     *
     * @return номер телефона
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Устанавливает номер телефона волонтёра.
     *
     * @param phoneNumber номер телефона
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Возвращает email волонтёра.
     *
     * @return email волонтёра
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает email волонтёра.
     *
     * @param email email волонтёра
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Возвращает дату рождения волонтёра.
     *
     * @return дата рождения
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Устанавливает дату рождения волонтёра.
     *
     * @param birthDate дата рождения
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Возвращает населённый пункт проживания волонтёра.
     *
     * @return населённый пункт проживания
     */
    public String getResidenceSettlement() {
        return residenceSettlement;
    }

    /**
     * Устанавливает населённый пункт проживания волонтёра.
     *
     * @param residenceSettlement населённый пункт проживания
     */
    public void setResidenceSettlement(String residenceSettlement) {
        this.residenceSettlement = residenceSettlement;
    }

    /**
     * Возвращает район населённого пункта проживания волонтёра.
     *
     * @return район населённого пункта проживания или {@code null}, если он не указан
     */
    public String getResidenceDistrict() {
        return residenceDistrict;
    }

    /**
     * Устанавливает район населённого пункта проживания волонтёра.
     *
     * @param residenceDistrict район населённого пункта проживания
     */
    public void setResidenceDistrict(String residenceDistrict) {
        this.residenceDistrict = residenceDistrict;
    }
}
