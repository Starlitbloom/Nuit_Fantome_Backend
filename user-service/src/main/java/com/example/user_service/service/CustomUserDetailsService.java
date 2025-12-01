package com.example.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.user_service.webclient.AuthClient;

@Service
public class CustomUserDetailsService {

    @Autowired
    private AuthClient authClient;

    public UserDetails loadUserByUsernameAndToken(String email, String token)
            throws UsernameNotFoundException {

        // OBTENER ID DESDE AUTHSERVICE
        Long userId = authClient.getUserIdByEmail(email, token);

        if (userId == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // OBTENER DATOS COMPLETOS DEL USUARIO
        var userMap = authClient.obtenerUsuarioPorId(userId, token);

        if (userMap == null || userMap.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // EXTRAER CAMPOS
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");

        // SPRING SECURITY NECESITA EL PREFIJO "ROLE_"
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        // CONSTRUIR USERDETAILS
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(password)
                .authorities(new SimpleGrantedAuthority(role))
                .build();
    }
}

