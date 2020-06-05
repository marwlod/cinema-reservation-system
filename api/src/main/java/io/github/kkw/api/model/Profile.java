package io.github.kkw.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kkw.api.db.dto.ProfileEntity;

public class Profile {
    private final String name;
    private final String surname;
    private final String email;
    private final boolean isAdmin;

    public Profile(@JsonProperty("name") String name,
                   @JsonProperty("surname") String surname,
                   @JsonProperty("email") String email,
                   @JsonProperty("isAdmin") boolean isAdmin) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public Profile(ProfileEntity profileEntity) {
        this(
                (profileEntity.getName() == null || profileEntity.getName().isBlank()) ? "Unknown name" : profileEntity.getName(),
                (profileEntity.getSurname() == null || profileEntity.getSurname().isBlank()) ? "Unknown surname" : profileEntity.getSurname(),
                profileEntity.getEmail(),
                profileEntity.isAdmin());
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
