package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.example.user_service.model.UserProfile;
import com.example.user_service.repository.UserProfileRepository;
import com.example.user_service.webclient.AuthClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthClient authClient;

    // ========================= CREAR PERFIL =========================
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public UserProfile crearPerfil(UserProfile perfil, Authentication auth) {

        String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String email = auth.getName();

        // 🔐 Obtener el ID real desde AuthService
        String token = "";
        Long requesterUserId = authClient.getUserIdByEmail(email, token);

        // Si el usuario es CLIENTE solo puede crear su propio perfil
        if (role.equals("CLIENTE") && !perfil.getUserId().equals(requesterUserId)) {
            throw new AccessDeniedException("No puedes crear un perfil para otro usuario");
        }

        return userProfileRepository.save(perfil);
    }

    // ========================= OBTENER PERFIL =========================
    public UserProfile obtenerPerfil(Long requesterId, Long targetUserId, String role) {
        if (!role.equals("ADMIN") && !requesterId.equals(targetUserId)) {
            throw new AccessDeniedException("No autorizado");
        }

        return userProfileRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    // ========================= ACTUALIZAR PERFIL =========================
    public UserProfile actualizarPerfilPorId(Long requesterId, Long targetUserId, UserProfile datos, String role) {

        if (!role.equals("ADMIN") && !requesterId.equals(targetUserId)) {
            throw new AccessDeniedException("No autorizado");
        }

        UserProfile perfil = userProfileRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        if (datos.getDireccion() != null) perfil.setDireccion(datos.getDireccion());
        if (datos.getTelefono() != null) perfil.setTelefono(datos.getTelefono());
        if (datos.getNombre() != null) perfil.setNombre(datos.getNombre());
        if (datos.getApellido() != null) perfil.setApellido(datos.getApellido());
        if (datos.getComuna() != null) perfil.setComuna(datos.getComuna());
        if (datos.getRegion() != null) perfil.setRegion(datos.getRegion());

        return userProfileRepository.save(perfil);
    }

    // ========================= ELIMINAR PERFIL =========================
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarPerfil(Long targetUserId) {

        UserProfile perfil = userProfileRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));

        userProfileRepository.delete(perfil);

        // Eliminar también en AuthService
        authClient.eliminarUsuario(targetUserId);
    }

    // ========================= MÉTODO PARA OBTENER USER ID POR EMAIL =========================
    public Long getUserIdFromEmail(String email) {
        String token = "";
        return authClient.getUserIdByEmail(email, token);
    }
}
