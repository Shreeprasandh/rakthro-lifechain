public class BloodRequest {
    public String name;
    public String city;
    public String bloodGroup;
    public String contact;
    public String email;

    // Constructor
    public BloodRequest(String name, String city, String bloodGroup, String contact, String email) {
        this.name = name;
        this.city = city;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.email = email;
    }

    // Convert to CSV format
    public String toCSV() {
        return String.join(",", name, city, bloodGroup, contact, email);
    }

    // Convert from CSV line to object
    public static BloodRequest fromCSV(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] parts = line.split(",");
        if (parts.length < 5) return null;

        return new BloodRequest(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
