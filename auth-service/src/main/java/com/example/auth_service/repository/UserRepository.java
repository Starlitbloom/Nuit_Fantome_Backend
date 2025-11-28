package com.example.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por email (necesario para registro y login)
    Optional<User> findByEmail(String email);

    // Para verificar si existe, puedes usar este query automático
    boolean existsByEmail(String email);
}
