package com.engine.starter.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.engine.core.domain.Role;
import com.engine.core.domain.User;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private String id;

    private String email;
    private String password;
    private String role;

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.getId();
        entity.email = user.getEmail();
        entity.password = user.getPasswordHash();
        entity.role = user.getRole().name();
        return entity;
    }

    public User toDomain() {
        return new User(id, email, password, Role.valueOf(role));
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}