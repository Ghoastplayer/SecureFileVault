package net.tim;

import net.tim.gui.UserNotFoundExeption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Scanner;
import java.util.Arrays;

public class UserManager {

    public static void createUser(String username, String password) throws UserAlreadyExistsException {
        User user = new User(username, password);

        //Check if username already exists
        if (new File("src/main/resources/users/"+ username + ".txt").exists()) {
            System.out.println("User already exists");
            throw new UserAlreadyExistsException("User already exists");
        }

        saveUserToFile(user, "src/main/resources/users/"+ username + ".txt");

        //Create a folder for the user in the vault
        File file = new File("src/main/resources/vault/" + username);
        if (!file.mkdir()) {
            System.out.println("Failed to create the user vault");
            throw new RuntimeException("Failed to create the user vault");
        }

        System.out.println("User created successfully");
    }

    public static void deleteUser(String username) {
        File file = new File("src/main/resources/users/"+ username + ".txt");
        if (file.delete()) {
            System.out.println("User deleted successfully");
        } else {
            System.out.println("Failed to delete the user");
            throw new RuntimeException("Failed to delete the user");
        }

        //Delete the user folder in the vault
        file = new File("src/main/resources/vault/" + username);
        if (!file.delete()) {
            System.out.println("Failed to delete the user vault");
            throw new RuntimeException("Failed to delete the user vault");
        }
    }

    public static void changePassword(String username, String newPassword) throws UserNotFoundExeption {
        User user = loadUserFromFile("src/main/resources/users/"+ username + ".txt");
        File file = new File("src/main/resources/users/"+ username + ".txt");
        if (user == null) {
            throw new UserNotFoundExeption("User not found");
        }
        if (!file.delete()) {
            System.out.println("Failed to delete the user");
            return;
        }

        user = new User(username, newPassword);
        saveUserToFile(user, "src/main/resources/users/"+ username + ".txt");
    }

    public static User getUser(String username) {
        return loadUserFromFile("src/main/resources/users/"+ username + ".txt");
    }

    public static boolean authorizeUser(String username, String password) {
        User user = getUser(username);
        if (user == null) {
            System.out.println("User not found");
            return false;
        }

        try {
            return user.getHashedPassword().equals(EncryptionService.getHash(password, Arrays.toString(user.getSalt())));
        } catch (Exception e) {
            System.out.println("Failed to authorize the user: " + e.getMessage());
            return false;
        }

    }

    private static void saveUserToFile(User user, String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Hashed password: " + user.getHashedPassword());
            System.out.println("Salt: " + Arrays.toString(user.getSalt()));
            System.out.println("IV: " + Arrays.toString(user.getIv()));

            out.println(user.getUsername());
            out.println(user.getHashedPassword());
            out.println(Base64.getEncoder().encodeToString(user.getSalt()));
            out.println(Base64.getEncoder().encodeToString(user.getIv()));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static User loadUserFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            String username = scanner.nextLine();
            String hashedPassword = scanner.nextLine();
            byte[] salt = Base64.getDecoder().decode(scanner.nextLine());
            byte[] iv = Base64.getDecoder().decode(scanner.nextLine());

            return new User(username, hashedPassword, salt, iv);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return null;
        }
    }

}
