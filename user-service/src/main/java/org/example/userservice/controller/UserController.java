package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.example.userservice.api.UserApi;
import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления пользователями.
 * Контроллер:
 * взаимодействует с сервисным уровнем {@link UserService};
 * формирует HATEOAS-ссылки для ответов;
 * реализует CRUD-операции, определённые в {@code UserApi}.
 */
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(this::toHateoasEntityModel)
                .toList();

        Link selfLink = linkTo(methodOn(UserController.class).getUsers()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(users, selfLink));
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<EntityModel<UserDto>> getUser(int id) {
        UserDto user = userService.getUserById(id);

        return ResponseEntity.ok(toHateoasEntityModel(user));
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<EntityModel<UserDto>> createUser(UserDto user) {
        UserDto createdUser = userService.createUser(user);
        EntityModel<UserDto> model = toHateoasEntityModel(createdUser);

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUser(createdUser.getId())).toUri())
                .body(model);
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<EntityModel<UserDto>> updateUser(int id, UserDto user) {
        UserDto updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(toHateoasEntityModel(updatedUser));
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> deleteUser(int id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Создание HATEOAS-обёртки
     *
     * @param user DTO пользователя
     * @return HATEOAS-модель пользователя
     */
    private EntityModel<UserDto> toHateoasEntityModel(UserDto user) {
        EntityModel<UserDto> model = EntityModel.of(user);
        addLinks(model);

        return model;
    }

    /**
     * Добавление HATEAOS-ссылок:
     * self — ссылка на текущего пользователя;
     * all-users — ссылка на список всех пользователей;
     * update — ссылка на обновление пользователя;
     * delete — ссылка на удаление пользователя.
     *
     * @param model HATEOAS-модель пользователя
     */
    private void addLinks(EntityModel<UserDto> model) {
        UserDto user = model.getContent();

        model.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"));
        model.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), user)).withRel("update"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
    }
}
