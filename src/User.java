import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;
    private int cardBalance;

    public int getCardBalance() {
        return cardBalance;
    }

    public User(String username, String password, boolean isAdmin, int cardBalance) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.cardBalance = cardBalance;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String toFileString() {
        return username + "," + password + "," + isAdmin + "," + cardBalance;
    }

    public static User fromFileString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;
        return new User(parts[0], parts[1], Boolean.parseBoolean(parts[2]), Integer.parseInt(parts[3]));
    }

    public void updateUserBalance(User user, int newBalance) {
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equals(user.getUsername()) && parts[1].equals(user.getPassword())) {
                    parts[3] = String.valueOf(newBalance);
                    updatedLines.add(String.join(",", parts));
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (String l : updatedLines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
