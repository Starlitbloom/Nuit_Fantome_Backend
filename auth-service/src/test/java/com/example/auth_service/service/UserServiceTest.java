package com.example.auth_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.Rol;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ---------------------------
    //       TEST REGISTER
    // ---------------------------

    @Test
    void register_Exitoso() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encodedPass");

        User saved = new User();
        saved.setId(1L);
        saved.setEmail("test@mail.com");
        saved.setPassword("encodedPass");
        saved.setRole(Rol.CLIENTE);
        saved.setActive(true);

        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.register(request);

        assertEquals("test@mail.com", result.getEmail());
        assertNull(result.getPassword());
        assertEquals(Rol.CLIENTE, result.getRole());
        assertTrue(result.isActive());
    }

    @Test
    void register_CorreoDuplicado() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new User()));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(request);
        });

        assertEquals("El correo ya está registrado", ex.getMessage());
    }

    @Test
    void register_DatosInvalidos() {
        RegisterRequest request = new RegisterRequest(); // vacío

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.register(request);
        });

        assertEquals("Datos de registro inválidos", ex.getMessage());
    }

    // ---------------------------
    //       TEST LOGIN
    // ---------------------------

    @Test
    void login_Exitoso() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "encoded")).thenReturn(true);

        User result = userService.login(request);

        assertEquals("test@mail.com", result.getEmail());
        assertNull(result.getPassword()); // password eliminado
    }

    @Test
    void login_UsuarioNoExiste() {
        LoginRequest request = new LoginRequest();
        request.setEmail("no@mail.com");
        request.setPassword("123");

        when(userRepository.findByEmail("no@mail.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(request);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void login_ContrasenaIncorrecta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@mail.com");
        request.setPassword("wrong");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(request);
        });

        assertEquals("Contraseña incorrecta", ex.getMessage());
    }
}
