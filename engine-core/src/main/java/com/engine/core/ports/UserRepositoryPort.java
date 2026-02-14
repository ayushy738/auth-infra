package com.engine.core.ports;

import com.engine.core.domain.User;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);
    void save(User user);
}