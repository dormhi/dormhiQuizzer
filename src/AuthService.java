import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages user authentication and registration operations.
 * Contains the business logic for Login and Register processes.
 */
public class AuthService {
    private static final String USER_FILE = "users.csv";
    private IDManager idManager;

    /**
     * Constructor for AuthService.
     * Initializes IDManager for permission checks.
     */
    public AuthService() {
        this.idManager = new IDManager();
    }

    /**
     * Authenticates a user into the system using username and password.
     *
     * @param username The username (Case insensitive)
     * @param password The password (Case sensitive)
     * @return A User object if login is successful, null otherwise.
     */
    public User login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                // Format: ID;ROLE;NAME;USERNAME;PASSWORD
                if (parts.length == 5) {
                    String fileUser = parts[3].trim();
                    String filePass = parts[4].trim();

                    // Check username (case insensitive) and password (exact match)
                    if (fileUser.equalsIgnoreCase(username.trim()) && filePass.equals(password.trim())) {
                        return createUserObject(parts);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File error during login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registers a new user.
     * First checks if the ID is authorized, then checks if it is already registered.
     *
     * @param id       Institution ID (Student/Teacher ID)
     * @param fullName Full Name
     * @param username Desired Username
     * @param password Desired Password
     * @return true if registration is successful, false otherwise.
     */
    public boolean register(String id, String fullName, String username, String password) {
        // 1. Authorization Check (Is this ID allowed?)
        if (!idManager.isIdAllowed(id)) {
            System.out.println("ERROR: This ID is not authorized for registration!");
            return false;
        }

        // 2. Duplicate Check (Is this user already registered?)
        if (isUserAlreadyRegistered(id)) {
            System.out.println("ERROR: This ID is already registered!");
            return false;
        }

        // 3. Save to File
        String role = idManager.getRole(id);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            String newUserLine = id + ";" + role + ";" + fullName + ";" + username + ";" + password;
            bw.write(newUserLine);
            bw.newLine(); // Ensure we move to the next line
            System.out.println("Registration Successful! You may now log in.");
            return true;
        } catch (IOException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Helper method: Creates a User object from a CSV line
    private User createUserObject(String[] parts) {
        String id = parts[0];
        String role = parts[1];
        String name = parts[2];
        String username = parts[3];
        String password = parts[4];

        // Locale.ENGLISH ensures correct capitalization for switch cases
        switch (role.toUpperCase(java.util.Locale.ENGLISH)) {
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

    // Helper method: Checks if the ID already exists in the database
    private boolean isUserAlreadyRegistered(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length > 0 && parts[0].trim().equals(id.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            // File might not exist yet, strictly not an error in this check context
        }
        return false;
    }

    public List<String> getAllRegisteredUsers() {
        List<String> userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    userList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read user list: " + e.getMessage());
        }
        return userList;
    }

    public boolean updateUserPassword(String targetId, String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // 1. Read file and load into memory
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");

                if (parts.length == 5 && parts[0].trim().equals(targetId)) {
                    // Update password part
                    String newLine = parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + parts[3] + ";" + newPassword;
                    lines.add(newLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("File reading error: " + e.getMessage());
            return false;
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
                return true;
            } catch (IOException e) {
                System.out.println("File writing error: " + e.getMessage());
                return false;
            }
        }
        return false; // User not found
    }
}