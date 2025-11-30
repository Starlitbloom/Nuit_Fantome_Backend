package com.example.auth_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.example.auth_service.config.JwtUtil;
import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.RegisterRequest;
import com.example.auth_service.model.Rol;
import com.example.auth_service.model.User;
import com.example.auth_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper mapper = new ObjectMapper();

    // ----------------------------
    // TEST: Registro OK
    // ----------------------------
    @Test
    void register_returnOK() throws Exception {

        RegisterRequest req = new RegisterRequest("test@mail.com", "1234");

        User u = new User();
        u.setId(1L);
        u.setEmail("test@mail.com");
        u.setPassword("1234");
        u.setRole(Rol.CLIENTE);

        Mockito.when(userService.register(Mockito.any()))
                .thenReturn(u);

        Mockito.when(userDetailsService.loadUserByUsername("test@mail.com"))
                .thenReturn(org.springframework.security.core.userdetails.User
                        .withUsername("test@mail.com")
                        .password("1234")
                        .authorities("ROLE_CLIENTE")
                        .build());

        Mockito.when(jwtUtil.generateToken(Mockito.any()))
                .thenReturn("mocked-token");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-token"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }

    // ----------------------------
    // TEST: Login OK
    // ----------------------------
    @Test
    void login_returnOK() throws Exception {

        LoginRequest req = new LoginRequest("test@mail.com", "1234");

        User u = new User();
        u.setId(1L);
        u.setEmail("test@mail.com");
        u.setPassword("1234");
        u.setRole(Rol.CLIENTE);

        Mockito.when(userService.login(Mockito.any()))
                .thenReturn(u);

        Mockito.when(userDetailsService.loadUserByUsername("test@mail.com"))
                .thenReturn(org.springframework.security.core.userdetails.User
                        .withUsername("test@mail.com")
                        .password("1234")
                        .authorities("ROLE_CLIENTE")
                        .build());

        Mockito.when(jwtUtil.generateToken(Mockito.any()))
                .thenReturn("mocked-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-token"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }
}
