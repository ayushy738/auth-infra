package com.engine.starter.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.engine.core.domain.User;
import com.engine.core.ports.UserRepositoryPort;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaRepo;

    public UserRepositoryAdapter(JpaUserRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public void save(User user) {
        jpaRepo.save(UserEntity.fromDomain(user));
    }
}