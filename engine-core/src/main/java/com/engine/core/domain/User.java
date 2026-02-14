package com.engine.core.domain;

public class User {

    private final String id;
    private final String email;
    private final String passwordHash;
    private final Role role;

    public User(String id, String email, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
}