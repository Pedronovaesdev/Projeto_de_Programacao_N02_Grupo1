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


}