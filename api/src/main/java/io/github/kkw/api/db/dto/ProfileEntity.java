package io.github.kkw.api.db.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProfileEntity {
    @Id
    private int clientId;
    private String name;
    private String surname;
    private String email;
    private boolean isAdmin;

    public ProfileEntity() {
    }

    public int getClientId() {
        return clientId;
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

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
