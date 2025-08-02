public class AuthSystem {
    private final String USERNAME = "Kaizzcer";
    private final String PASSWORD = "SaveLife";

    public boolean login(String u, String p) {
        return USERNAME.equals(u) && PASSWORD.equals(p);
    }
}
