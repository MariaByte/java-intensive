package org.example.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit-тесты для {@link UserController} выполнены с использованием MockMvc и Mockito.
 * Проверяется работа CRUD операций контроллера.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto testUser;

    /**
     * Создание тестового объекта пользователя перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        testUser = new UserDto();
        testUser.setId(1);
        testUser.setName("Test");
        testUser.setEmail("user@mail.ru");
        testUser.setAge(25);
    }

    /**
     * Проверяет создание пользователя через POOST /api/users.
     *
     * @throws Exception
     */
    @Test
    void create_shouldPersistUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
    }

    /**
     * Проверяет получение существующего пользователя через GET /api/users/{id}.
     *
     * @throws Exception
     */
    @Test
    void getById_existingUser_shouldReturnUser() throws Exception {
        when(userService.getUserById(1)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value(testUser.getName()));
    }

    /**
     * Проверяет получение несуществующего пользователя через GET /api/users/{id}.
     *
     * @throws Exception
     */
    @Test
    void getById_nonExistingUser_shouldReturnNotFound() throws Exception {
        when(userService.getUserById(999)).thenReturn(null);

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Проверяет получение всех пользователей через GET /api/users.
     *
     * @throws Exception
     */
    @Test
    void getAll_whenUsersExist_shouldReturnList() throws Exception {
        UserDto user2 = new UserDto();
        user2.setId(2);
        user2.setName("Another User");
        user2.setEmail("user2@mail.ru");
        user2.setAge(30);

        List<UserDto> users = Arrays.asList(testUser, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(testUser.getId()))
                .andExpect(jsonPath("$._embedded.userDtoList[1].id").value(user2.getId()));
    }

    /**
     * Проверяет обновление существующего пользователя через PUT /api/users/{id}.
     *
     * @throws Exception
     */
    @Test
    void update_shouldModifyExistingUser() throws Exception {
        testUser.setName("Updated Name");
        when(userService.updateUser(eq(1), any(UserDto.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    /**
     * Проверяет удаление существующего пользователя через DELETE /api/users/{id}.
     *
     * @throws Exception
     */
    @Test
    void delete_existingUser_shouldReturnOk() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Проверяет удаление несуществующего пользователя через DELETE /api/users/{id}.
     *
     * @throws Exception
     */
    @Test
    void delete_nonExistingUser_shouldNotThrow() throws Exception {
        doNothing().when(userService).deleteUser(9999);

        mockMvc.perform(delete("/api/users/9999"))
                .andExpect(status().isNoContent());
    }
}
