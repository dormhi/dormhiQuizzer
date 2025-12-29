import java.io.*;

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
            System.out.println("Giris sirasinda dosya hatasi: " + e.getMessage());
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
            System.out.println("HATA: Bu numaranin kayit yetkisi yok!");
            return false;
        }

        // 2. Duplicate Check (Is this user already registered?)
        if (isUserAlreadyRegistered(id)) {
            System.out.println("HATA: Bu numara zaten kayitli!");
            return false;
        }

        // 3. Save to File
        String role = idManager.getRole(id);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            String newUserLine = id + ";" + role + ";" + fullName + ";" + username + ";" + password;
            bw.write(newUserLine);
            bw.newLine(); // Ensure we move to the next line
            System.out.println("Kayit Basarili! Giris yapabilirsiniz.");
            return true;
        } catch (IOException e) {
            System.out.println("Kayit hatasi: " + e.getMessage());
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
            // Ignore errors during read check
        }
        return false;
    }
}