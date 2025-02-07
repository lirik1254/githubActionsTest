package org.example.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userservice.controllers.UserController;
import org.example.userservice.models.Users;
import org.example.userservice.repos.UserRepository;
import org.example.userservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Users user;

    @BeforeEach
    public void setup() {
        user = Users.builder()
                .userId(1)
                .name("John Doe")
                .email("johndoe@example.com")
                .password("password123")
                .role("USER")
                .profilePicture("profilepic.jpg")
                .build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(user));
        userService.getAllUsers().forEach(s -> System.out.println(s.getUserId()));
        mockMvc.perform(get("/usersа"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Подготовка данных
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.empty());


        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        // Подготовка данных
        Users newUser = Users.builder()
                .name("Jane Doe")
                .email("janedoe@example.com")
                .password("password123")
                .role("ADMIN")
                .profilePicture("janedoe.jpg")
                .build();

        when(userRepository.save(any(Users.class))).thenReturn(newUser);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("janedoe@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Подготовка данных
        Users updatedUser = Users.builder()
                .name("John Smith")
                .email("johnsmith@example.com")
                .password("newpassword")
                .role("USER")
                .profilePicture("johnsmith.jpg")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        // Выполнение запроса и проверка результата
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("johnsmith@example.com"));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        // Подготовка данных
        Users updatedUser = Users.builder()
                .name("John Smith")
                .email("johnsmith@example.com")
                .password("newpassword")
                .role("USER")
                .profilePicture("johnsmith.jpg")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Выполнение запроса и проверка результата
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Подготовка данных
        when(userRepository.existsById(1)).thenReturn(true);

        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        // Подготовка данных
        when(userRepository.existsById(1)).thenReturn(false);

        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllUsers_Empty() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
