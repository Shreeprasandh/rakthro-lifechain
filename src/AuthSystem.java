public class AuthSystem {
    // Use static final for constants that don't change per instance
    private static final String USERNAME = "Kaizzcer";
    private static final String PASSWORD = "SaveLife";

    /**
     * Authenticates a user based on provided username and password.
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
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}
