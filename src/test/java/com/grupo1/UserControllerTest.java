package com.grupo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo1.business.UserService;
import com.grupo1.controller.UserController;
import com.grupo1.dto.UserRequestDTO;
import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequestDTO validUserRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validUserRequest = new UserRequestDTO(
                "John Doe", "john.doe@test.com", "12345678900",
                "password123", "987654321", "Programming",
                LocalDate.of(1990, 1, 1), Role.STUDENT, "REG12345", null
        );

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("John Doe");
        mockUser.setEmail("john.doe@test.com");
    }

    @Test //Salvar usuário
    @DisplayName("POST /user - Deve salvar o usuário e retornar 200 OK")
    void saveUser_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        doNothing().when(userService).saveUser(any(User.class));

        // Act & Assert
        ObjectMapper objectMapper = null; //inicializar a variável
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test //Buscar por Email
    @DisplayName("GET /user - ADMIN - Deve retornar o usuário e 200 OK")
    void findByEmail_Admin_ShouldReturnUserAnd200Ok() throws Exception {
        // Arrange
        when(userService.findByUserEmail(eq("john.doe@test.com"))).thenReturn(mockUser);

        // Act & Assert (Simula ADMIN)
        mockMvc.perform(get("/user")
                        .param("email", "john.doe@test.com")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john.doe@test.com")));

        verify(userService, times(1)).findByUserEmail(eq("john.doe@test.com"));
    }

    @Test
    @DisplayName("GET /user - Não ADMIN - Deve retornar 403 Forbidden (Cenário de Erro/Segurança)")
    void findByEmail_NonAdmin_ShouldReturn403Forbidden() throws Exception {
        // Act & Assert (Simula STUDENT)
        mockMvc.perform(get("/user")
                        .param("email", "john.doe@test.com")
                        .with(SecurityMockMvcRequestPostProcessors.user("student@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_STUDENT")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(userService, never()).findByUserEmail(anyString());
    }

    @Test //Buscar Todos
    @DisplayName("GET /user/all - ADMIN - Deve retornar a lista de usuários e 200 OK")
    void findAll_Admin_ShouldReturnUserListAnd200Ok() throws Exception {
        // Arrange
        List<User> userList = Arrays.asList(mockUser, new User());
        when(userService.findByAll()).thenReturn(userList);

        // Act & Assert (Simula ADMIN)
        mockMvc.perform(get("/user/all")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(userService, times(1)).findByAll();
    }

    @Test //Atualizar Usuário
    @DisplayName("PUT /user/{id} - ADMIN - Deve atualizar e retornar 200 OK")
    void updateUser_Admin_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        Integer userId = 1;
        doNothing().when(userService).updateUserById(eq(userId), any(User.class));

        // Act & Assert (Simula ADMIN)
        ObjectMapper objectMapper = null;
        mockMvc.perform(put("/user/{id}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserById(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("PUT /user/{id} - INSTRUCTOR - Deve atualizar e retornar 200 OK (Caminho Feliz)")
    void updateUser_Instructor_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        Integer userId = 1;
        doNothing().when(userService).updateUserById(eq(userId), any(User.class));

        // Act & Assert (Simula INSTRUCTOR)
        ObjectMapper objectMapper = null;
        mockMvc.perform(put("/user/{id}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("instructor@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_INSTRUCTOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserById(eq(userId), any(User.class));
    }

    @Test
    @DisplayName("PUT /user/{id} - STUDENT - Deve retornar 403 Forbidden (Cenário de Erro/Segurança)")
    void updateUser_Student_ShouldReturn403Forbidden() throws Exception {
        // Arrange
        Integer userId = 1;

        // Act & Assert (Simula STUDENT)
        ObjectMapper objectMapper = null;
        mockMvc.perform(put("/user/{id}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("student@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_STUDENT")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isForbidden());

        verify(userService, never()).updateUserById(eq(userId), any(User.class));
    }

    @Test //Deletar por Email
    @DisplayName("DELETE /user - ADMIN - Deve deletar por email e retornar 200 OK (Caminho Feliz)")
    void deleteUserByEmail_Admin_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        String email = "john.doe@test.com";
        doNothing().when(userService).deleteByEmail(eq(email));

        // Act & Assert (Simula ADMIN)
        mockMvc.perform(delete("/user")
                        .param("email", email)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteByEmail(eq(email));
    }

    @Test
    @DisplayName("DELETE /user - Não ADMIN - Deve retornar 403 Forbidden (Cenário de Erro/Segurança)")
    void deleteUserByEmail_NonAdmin_ShouldReturn403Forbidden() throws Exception {
        // Act & Assert (Simula STUDENT)
        mockMvc.perform(delete("/user")
                        .param("email", "john.doe@test.com")
                        .with(SecurityMockMvcRequestPostProcessors.user("student@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteByEmail(anyString());
    }

    @Test //Deletar por ID
    @DisplayName("DELETE /user/{id} - ADMIN - Deve deletar por ID e retornar 200 OK (Caminho Feliz)")
    void deleteUserById_Admin_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        Integer userId = 1;
        doNothing().when(userService).deleteById(eq(userId));

        // Act & Assert (Simula ADMIN)
        mockMvc.perform(delete("/user/{id}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById(eq(userId));
    }

    @Test //Atualizar Role
    @DisplayName("PATCH /user/{id}/role - ADMIN - Deve atualizar a role e retornar 200 OK (Caminho Feliz)")
    void updateUserRole_Admin_ShouldReturn200Ok_OnSuccess() throws Exception {
        // Arrange
        Integer userId = 1;
        Role newRole = Role.INSTRUCTOR;

        doNothing().when(userService).updateUserById(eq(userId), any(User.class));

        // Act & Assert (Simula ADMIN)
        mockMvc.perform(patch("/user/{id}/role", userId)
                        .param("role", newRole.name())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.com")
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserById(eq(userId), any(User.class));
    }
}