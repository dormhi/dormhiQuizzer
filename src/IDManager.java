import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IDManager {
    private static final String FILE_NAME = "allowed_ids.csv";
    private Map<String, String> allowedIds;

    public IDManager() {
        allowedIds = new HashMap<>();
        loadIds(); //
    }

    private void loadIds() {
        allowedIds.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    allowedIds.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load allowed IDs: " + e.getMessage());
        }
    }

    private void saveIds() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : allowedIds.entrySet()) {
                bw.write(entry.getKey() + ";" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving IDs: " + e.getMessage());
        }
    }


    public boolean isIdAllowed(String id) {
        return allowedIds.containsKey(id);
    }

    public String getRole(String id) {
        return allowedIds.get(id);
    }

    public void addId(String id, String role) {
        allowedIds.put(id, role.toUpperCase());
        saveIds();
        System.out.println("ID added successfully: " + id + " (" + role + ")");
    }

    public void removeId(String id) {
        if (allowedIds.containsKey(id)) {
            allowedIds.remove(id);
            saveIds();
            System.out.println("ID removed successfully: " + id);
        } else {
            System.out.println("ID not found!");
        }
    }
    public void listAllIds() {
        System.out.println("\n--- SISTEMDEKI IZINLI NUMARALAR ---");
        if (allowedIds.isEmpty()) {
            System.out.println("Liste bos!");
        } else {
            for (Map.Entry<String, String> entry : allowedIds.entrySet()) {
                System.out.println(">> ID: " + entry.getKey() + "  |  ROL: " + entry.getValue());
            }
        }
        System.out.println("-----------------------------------");
    }
}