package services;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;
import java.util.List;
import java.util.Scanner;

public class UserConsoleService {

    private final UserDao userDao = new UserDaoImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();

            System.out.print("Пункт: ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> listUsers();
                    case 3 -> getUserById();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 0 -> running = false;
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
        System.out.println("До свидания!");
    }

    private void printMenu() {
        System.out.println("\n================= МЕНЮ =================");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Просмотреть всех пользователей");
        System.out.println("3. Найти пользователя по ID");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.println("========================================");
    }

    private void createUser() {
        System.out.println("\n=== Создание пользователя ===");
        String name = readNonEmptyString("Имя: ");
        String email = readNonEmptyString("Email: ");
        int age = readInt("Возраст: ");

        userDao.create(new User(name, email, age));
        System.out.println("Пользователь создан!");
    }

    private void listUsers() {
        List<User> users = userDao.getAll();
        if (users.isEmpty()) {
            System.out.println("Нет пользователей.");
            return;
        }

        System.out.println("\n================= СПИСОК ПОЛЬЗОВАТЕЛЕЙ =================");
        System.out.printf("%-5s | %-20s | %-25s | %-6s | %-20s%n",
                "ID", "Имя", "Email", "Возраст", "Дата создания");
        System.out.println("---------------------------------------------------------------");

        for (User user : users) {
            System.out.printf("%-5d | %-20s | %-25s | %-6d | %-20s%n",
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getAge(),
                    user.getCreatedAt() != null ? user.getCreatedAt().toString() : "-");
        }
        System.out.println("===============================================================");
    }

    private void getUserById() {
        int id = readInt("Введите ID: ");
        User user = userDao.getById(id);
        if (user != null) {
            System.out.println("\n🔎 Найден пользователь:");
            System.out.println(user);
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    private void updateUser() {
        int id = readInt("Введите ID: ");
        User user = userDao.getById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.println("Введите новые данные (оставьте пустым, если не надо менять):");

        System.out.print("Имя (" + user.getName() + "): ");
        String newName = scanner.nextLine();
        if (!newName.isBlank()) user.setName(newName);

        System.out.print("Email (" + user.getEmail() + "): ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isBlank()) user.setEmail(newEmail);

        System.out.print("Возраст (" + user.getAge() + "): ");
        String newAgeStr = scanner.nextLine();
        if (!newAgeStr.isBlank()) {
            try {
                user.setAge(Integer.parseInt(newAgeStr));
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть числом.");
            }
        }

        userDao.update(user);
        System.out.println("Пользователь обновлён!");
    }

    private void deleteUser() {
        int id = readInt("Введите ID пользователя: ");
        userDao.delete(id);
        System.out.println("🗑️  Пользователь удалён.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
            }
        }
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Поле не может быть пустой строкой. Попробуйте снова.");
        }
    }
}
