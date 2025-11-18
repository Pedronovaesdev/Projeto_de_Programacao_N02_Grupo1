package com.grupo1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo1.business.UserService;
import com.grupo1.controller.UserController;
import com.grupo1.dto.UserRequestDTO;
import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)

@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private UserService userService;


    @Configuration
    static class TestConfig {
        @Bean
        public UserService userService() {

            return mock(UserService.class);
        }
    }

    private UserRequestDTO createUserRequestDTO() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Novo Aluno");
        dto.setEmail("novo.aluno@email.com");
        dto.setCpf("12345678901");
        dto.setPassword("senhaSegura123");
        dto.setPhone("11987654321");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setRole(Role.STUDENT);
        dto.setRegistration("REG2024");
        return dto;
    }

    @Test
    @DisplayName("POST /user - Deve criar um novo usuário com sucesso (Acesso Público)")
    void saveUser_ShouldReturnOkStatus() throws Exception {
        UserRequestDTO userDto = createUserRequestDTO();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("POST /user - Deve retornar BAD_REQUEST para validação de dados falha (DTO)")
    void saveUser_ShouldReturnBadRequestOnValidationFailure() throws Exception {

        UserRequestDTO invalidDto = createUserRequestDTO();
        invalidDto.setEmail("email_invalido");
        invalidDto.setName("");

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Erro de validação")));

        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/all - Deve listar todos os usuários (Acesso ADMIN)")
    void findAll_ShouldReturnUsers_WhenUserIsAdmin() throws Exception {

        User admin = User.builder().id(1).email("admin@test.com").name("Admin").role(Role.ADMIN).build();
        User student = User.builder().id(2).email("student@test.com").name("Student").role(Role.STUDENT).build();
        List<User> userList = Arrays.asList(admin, student);

        when(userService.findByAll()).thenReturn(userList);


        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].email", is("admin@test.com")));

        verify(userService, times(1)).findByAll();
    }

    @Test
    @WithMockUser(roles = {"STUDENT"})
    @DisplayName("GET /user/all - Deve retornar 403 Forbidden para não-admin")
    void findAll_ShouldReturnForbidden_WhenUserIsNotAdmin() throws Exception {

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).findByAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("PATCH /user/{id}/role - Deve atualizar a Role de um usuário (Acesso ADMIN)")
    void updateUserRole_ShouldReturnOkStatus_WhenUserIsAdmin() throws Exception {

        Integer userId = 1;
        Role newRole = Role.INSTRUCTOR;


        mockMvc.perform(patch("/user/{id}/role", userId)
                        .param("role", newRole.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(userService, times(1)).updateUserById(eq(userId), any(User.class));
    }

    @Test
    @WithMockUser(roles = {"INSTRUCTOR"})
    @DisplayName("PATCH /user/{id}/role - Deve retornar 403 Forbidden para não-admin")
    void updateUserRole_ShouldReturnForbidden_WhenUserIsNotAdmin() throws Exception {

        Integer userId = 1;
        Role newRole = Role.INSTRUCTOR;


        mockMvc.perform(patch("/user/{id}/role", userId)
                        .param("role", newRole.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).updateUserById(anyInt(), any(User.class));
    }
}
