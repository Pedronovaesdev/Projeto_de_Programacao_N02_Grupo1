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
}