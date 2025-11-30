package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.entity.UserEntity;
import org.example.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный слой для управления пользователями.
 * Отвечает за бизнес-логику приложения, связанную с CRUD операциями над пользователями.
 * Работает с {@link UserRepository}.
 * Выполняет валидацию данных и преобразует данные между {@link UserEntity} и {@link UserDto}.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;

    public UserServiceImpl(UserRepository userRepository,
                           NotificationProducer notificationProducer) {
        this.userRepository = userRepository;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = new UserEntity(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );

        UserEntity savedUserEntity = userRepository.save(userEntity);

        notificationProducer.sendUserCreatedNotification(savedUserEntity.getEmail());

        return toDto(savedUserEntity);
    }

    @Override
    public UserDto getUserById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с ID " + id + " не найден"
                ));

        return toDto(userEntity);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с ID " + id + " не найден"
                ));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setAge(userDto.getAge());

        UserEntity updatedUserEntity = userRepository.save(existingUser);

        return toDto(updatedUserEntity);
    }

    @Override
    public void deleteUser(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь с ID " + id + " не найден"
                ));

        userRepository.deleteById(id);

        notificationProducer.sendUserDeletedNotification(user.getEmail());
    }

    private UserDto toDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getAge(),
                userEntity.getCreatedAt()
        );
    }
}