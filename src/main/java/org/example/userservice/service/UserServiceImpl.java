package org.example.userservice.service;

import org.example.userservice.dto.UserDto;
import org.example.userservice.entity.UserEntity;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный слой для управления пользователями.
 * Отвечает за бизнес-логику приложения, связанную с CRUD операциями над пользователями.
 * Работает с {@link UserRepository}.
 * Выполняет валидацию данных и преобразует данные между {@link UserEntity} и {@link UserDto}.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        validateUserData(userDto);

        UserEntity userEntity = new UserEntity(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return toDto(savedUserEntity);
    }

    public UserDto getUserById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));

        return toDto(userEntity);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(int id, UserDto userDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));

        validateUserData(userDto);

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setAge(userDto.getAge());

       UserEntity updatedUserEntity = userRepository.save(existingUser);

       return toDto(updatedUserEntity);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    private  void validateUserData(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (userDto.getAge() < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
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
