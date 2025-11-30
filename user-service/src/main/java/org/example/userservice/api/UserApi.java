package org.example.userservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.userservice.dto.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API-интерфейс для управления пользователями.
 * Содержит определение REST-эндпоинтов и аннотации OpenAPI/Swagger.
 *
 * Предоставляет операции:
 * GET /api/users — получение всех пользователей
 * GET /api/users/{id} — получение пользователя по ID
 * POST /api/users — создание нового пользователя
 * PUT /api/users/{id} — обновление пользователя по ID
 * DELETE /api/users/{id} — удаление пользователя по ID
 */
@Tag(name = "Пользователи", description = "Методы для управления пользователями")
@RequestMapping("/api/users")
public interface UserApi {

    /**
     * Получение всех пользователей.
     *
     * @return коллекция HATEOAS-моделей пользователей
     */
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен.")
    @GetMapping
    ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsers();

    /**
     * Получение пользователя по ID.
     *
     * @param id пользователя
     * @return HATEOAS-модель пользователя
     */
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    ResponseEntity<EntityModel<UserDto>> getUser(
            @Parameter(description = "ID пользователя, данные по которому запрашиваются", required = true)
            @PathVariable("id") int id);

    /**
     * Создание нового пользователя.
     *
     * @param user данные нового пользователя
     * @return созданный пользователь с HATEOAS-ссылками
     */
    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя")
    })
    @PostMapping
    ResponseEntity<EntityModel<UserDto>> createUser(
            @Parameter(description = "Данные нового пользователя")
            @Valid @RequestBody UserDto user);

    /**
     * Обновление пользователя по ID.
     *
     * @param id пользователя
     * @param user новые данные пользователя
     * @return обновлённый пользователь с HATEOAS-ссылками
     */
    @Operation(summary = "Обновить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")

    })
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<UserDto>> updateUser(
            @Parameter(description = "ID пользователя, данные для которого обновятся", required = true)
            @PathVariable("id") int id,
            @Valid @RequestBody UserDto user);

    /**
     * Удаление пользователя по ID.
     *
     * @param id пользователя
     * @return пустой ответ с кодом 204 при успешном удалении
     */
    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён."),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден.")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя, данные для которого обновятся", required = true)
            @PathVariable("id") int id);
}
