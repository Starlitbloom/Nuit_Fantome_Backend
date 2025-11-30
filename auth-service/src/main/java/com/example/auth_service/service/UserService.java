package com.example.auth_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.Rol;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // REGISTRO
    public User register(RegisterRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Datos de registro inválidos");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar rol por defecto y marcar activo
        user.setRole(Rol.CLIENTE);
        user.setActive(true);

        User saved = userRepository.save(user);
        // No devolver la contraseña
        saved.setPassword(null);
        return saved;
    }

    // LOGIN
    public User login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        Optional<User> opt = userRepository.findByEmail(request.getEmail());
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        User user = opt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        return user;
    }
}
