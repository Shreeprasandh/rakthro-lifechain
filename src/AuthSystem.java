import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthSystem {
    /**
     * Authenticates a user based on provided username and password by checking against admin.csv.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean login(String username, String password) {
        // Basic input validation to prevent NullPointerException
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false; // Cannot log in with empty or null credentials
        }

        try (BufferedReader br = new BufferedReader(new FileReader("db/admin.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].trim().equals(username.trim()) && parts[1].trim().equals(password.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false; // File read error, treat as invalid
        }
        return false;
    }

    /**
     * Authenticates a user by DonorID and password from user.csv.
     */
    public boolean userLoginByDonorId(String donorId, String password) {
        if (donorId == null || donorId.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("db/user.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(donorId) && parts[3].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    /**
     * Authenticates a user by Username, Email, and password from user.csv.
     */
    public boolean userLoginByCredentials(String username, String email, String password) {
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("db/user.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equals(username) && parts[2].equals(email) && parts[3].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
