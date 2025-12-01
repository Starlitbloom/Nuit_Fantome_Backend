package com.example.user_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.example.user_service.model.UserProfile;
import com.example.user_service.repository.UserProfileRepository;
import com.example.user_service.webclient.AuthClient;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private UserProfileService userProfileService;


    // ========================= OBTENER PERFIL =========================
    @Test
    void obtenerPerfil_AdminPuedeVerCualquiera() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(20L);

        UserProfile result = new UserProfile();
        result.setUserId(20L);

        when(userProfileRepository.findByUserId(20L)).thenReturn(Optional.of(perfil));

        // ADMIN puede ver cualquier perfil
        UserProfile perfilObtenido = userProfileService.obtenerPerfil(999L, 20L, "ADMIN");

        assertEquals(20L, perfilObtenido.getUserId());
    }

    @Test
    void obtenerPerfil_ClienteNoPuedeVerOtro() {
        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            userProfileService.obtenerPerfil(10L, 20L, "CLIENTE");
        });

        assertEquals("No autorizado", ex.getMessage());
    }

    @Test
    void obtenerPerfil_NoExiste_Error() {
        when(userProfileRepository.findByUserId(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.obtenerPerfil(5L, 5L, "CLIENTE");
        });

        assertEquals("Perfil no encontrado", ex.getMessage());
    }

    // ========================= ACTUALIZAR PERFIL =========================
    @Test
    void actualizarPerfil_ClienteActualizaSuPerfil_Exitoso() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(10L);
        perfil.setTelefono("111");

        UserProfile nuevosDatos = new UserProfile();
        nuevosDatos.setTelefono("999");

        when(userProfileRepository.findByUserId(10L)).thenReturn(Optional.of(perfil));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile actualizado = userProfileService.actualizarPerfilPorId(10L, 10L, nuevosDatos, "CLIENTE");

        assertEquals("999", actualizado.getTelefono());
    }

    @Test
    void actualizarPerfil_ClienteEditaOtroPerfil_Error() {
        UserProfile nuevosDatos = new UserProfile();

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> {
            userProfileService.actualizarPerfilPorId(10L, 20L, nuevosDatos, "CLIENTE");
        });

        assertEquals("No autorizado", ex.getMessage());
    }

    @Test
    void actualizarPerfil_NoExiste_Error() {
        when(userProfileRepository.findByUserId(10L)).thenReturn(Optional.empty());

        UserProfile nuevosDatos = new UserProfile();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.actualizarPerfilPorId(10L, 10L, nuevosDatos, "CLIENTE");
        });

        assertEquals("Perfil no encontrado", ex.getMessage());
    }

    // ========================= ELIMINAR PERFIL =========================
    @Test
    void eliminarPerfil_Admin_Exitoso() {
        UserProfile perfil = new UserProfile();
        perfil.setUserId(30L);

        when(userProfileRepository.findByUserId(30L)).thenReturn(Optional.of(perfil));
        doNothing().when(userProfileRepository).delete(perfil);
        doNothing().when(authClient).eliminarUsuario(30L);

        assertDoesNotThrow(() -> userProfileService.eliminarPerfil(30L));
    }

    @Test
    void eliminarPerfil_NoExiste_Error() {
        when(userProfileRepository.findByUserId(30L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userProfileService.eliminarPerfil(30L);
        });

        assertEquals("Perfil no encontrado", ex.getMessage());
    }

    // ========================= OBTENER USERID DESDE EMAIL =========================
    @Test
    void getUserIdFromEmail_Exitoso() {
        when(authClient.getUserIdByEmail("test@mail.com", "")).thenReturn(42L);

        Long userId = userProfileService.getUserIdFromEmail("test@mail.com");

        assertEquals(42L, userId);
    }
}
