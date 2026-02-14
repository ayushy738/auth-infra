package com.engine.starter.defaults;

import com.engine.core.domain.User;
import com.engine.core.ports.UserRepositoryPort;

import java.util.*;

public class InMemoryUserRepository implements UserRepositoryPort {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}