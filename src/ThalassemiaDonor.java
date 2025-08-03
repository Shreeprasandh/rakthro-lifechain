public class ThalassemiaDonor {
    String donorId;
    String name;
    int age;
    String city;
    String contact;
    String thalType; // Alpha / Beta
    String lastCheckupDate;

    public ThalassemiaDonor(String donorId, String name, int age, String city, String contact, String thalType, String lastCheckupDate) {
        this.donorId = donorId;
        this.name = name;
        this.age = age;
        this.city = city;
        this.contact = contact;
        this.thalType = thalType;
        this.lastCheckupDate = lastCheckupDate;
    }

    public String toCSV() {
        return String.join(",", donorId, name, String.valueOf(age), city, contact, thalType, lastCheckupDate);
    }

    public static ThalassemiaDonor fromCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length != 7) return null;
        return new ThalassemiaDonor(
            parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()),
            parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim()
        );
    }
}
