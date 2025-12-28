import java.io.*;
import java.util.Scanner;

public class AuthService {
    private static final String USER_FILE = "users.csv";
    private IDManager idManager;

    public AuthService() {
        this.idManager = new IDManager();
    }

    public User login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String fileUser = parts[3].trim();
                    String filePass = parts[4].trim();
                    if (fileUser.equalsIgnoreCase(username.trim()) && filePass.equals(password.trim())) {
                        return createUserObject(parts);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    public boolean register(String id, String fullName, String username, String password) {
        //ID permission control
        if (!idManager.isIdAllowed(id)) {
            System.out.println("Registration Failed: ID is not authorized!");
            return false;
        }

        if (isUserAlreadyRegistered(id)) {
            System.out.println("Registration Failed: User already registered!");
            return false;
        }

        String role = idManager.getRole(id);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            String newUserLine = id + ";" + role + ";" + fullName + ";" + username + ";" + password;

            bw.write(newUserLine); // Veriyi yaz
            bw.newLine();          // MUTLAKA ENTER'A BAS (Satır atla)

            System.out.println("Registration Successful! Please login.");
            return true;
        }


        catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    private User createUserObject(String[] parts) {
        String id = parts[0];
        String role = parts[1];
        String name = parts[2];
        String username = parts[3];
        String password = parts[4];

        switch (role.toUpperCase()) {
            case "STUDENT":
                return new Student(id, name, username, password);
            case "TEACHER":
                return new Teacher(id, name, username, password);
            case "ADMIN":
                return new Admin(id, name, username, password);
            default:
                return null;
        }
    }

    //ID check
    private boolean isUserAlreadyRegistered(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(id + ";")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}