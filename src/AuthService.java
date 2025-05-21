import java.io.*;
import java.util.*;

public class AuthService {
    private static final String USERS_FILE = "users.txt";
    private static final String FILE_PATH = new Files(USERS_FILE).readFilePath();
    AuthService() {
        
    }
    public static boolean register(String username, String password, String isAdmin, int cardBalance) {
        boolean isAdminFlag = isAdmin.trim().equalsIgnoreCase("yes");

        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("❌ Username already exists!");
                return false;
            }
        }

        User newUser = new User(username, password, isAdminFlag, cardBalance);
        users.add(newUser);
        saveUsers(users);

        System.out.println("✅ Registration successful!");
        return true;
    }

    public static User login(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("✅ Login successful!");
                return user;
            }
        }

        System.out.println("❌ Invalid username or password.");
        return null;
    }

    private static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromFileString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            //nothing
        }
        return users;
    }

    public static User getUserDetails (User user) {
        List<User> users = loadUsers();
        User details = null;
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                details = u;
            }
        }
        return details;
    }
    private static void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save users.");
        }
    }
}
