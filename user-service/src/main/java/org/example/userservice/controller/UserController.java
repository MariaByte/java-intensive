package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Контроллер для управления пользователями.
 * Предоставляет CRUD операции HTTP-методами:
 * GET /api/users — получение всех пользователей
 * GET /api/users/{id} — получение пользователя по ID
 * POST /api/users — создание нового пользователя
 * PUT /api/users/{id} — обновление пользователя по ID
 * DELETE /api/users/{id} — удаление пользователя по ID
 */
@RestController
@Tag(name = "Пользователи", description = "Методы для управления пользователями")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение списка всех пользователей.
     */
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsers() {

        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(this::toHateoasEntityModel)
                .toList();

        Link selfLink = linkTo(methodOn(UserController.class).getUsers()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(users, selfLink));
    }

    /**
     * Получение пользователя по ID.
     */
    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUser(
            @Parameter(description = "ID пользователя, данные по которому запрашиваются", required = true)
            @PathVariable("id") int id) {

        UserDto user = userService.getUserById(id);

        return ResponseEntity.ok(toHateoasEntityModel(user));
    }

    /**
     * Создание нового пользователя.
     */
    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя")
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(
            @Parameter(description = "Данные нового пользователя")
            @Valid @RequestBody UserDto user) {

        UserDto createdUser = userService.createUser(user);
        EntityModel<UserDto> model = toHateoasEntityModel(createdUser);

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUser(createdUser.getId())).toUri())
                .body(model);
    }

    /**
     * Обновление пользователя по ID.
     */
    @Operation(summary = "Обновить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")

    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @Parameter(description = "ID пользователя, данные для которого обновятся", required = true)
            @PathVariable("id") int id,
            @Valid @RequestBody UserDto user) {

        UserDto updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(toHateoasEntityModel(updatedUser));
    }

    /**
     * Удаление пользователя по ID.
     */
    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён."),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя, данные для которого обновятся", required = true)
            @PathVariable("id") int id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Создание HATEOAS-обёртки
     */
    private EntityModel<UserDto> toHateoasEntityModel(UserDto user) {
        EntityModel<UserDto> model = EntityModel.of(user);
        addLinks(model);

        return model;
    }

    /**
     * Добавление HATEAOS-ссылок
     */
    private void addLinks(EntityModel<UserDto> model) {
        UserDto user = model.getContent();

        model.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"));
        model.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), user)).withRel("update"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
    }
}
