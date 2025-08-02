import java.io.*;
import java.util.*;

public class HospitalDirectory {
    private final Map<String, List<String>> cityHospitalMap = new HashMap<>();
    private final String filePath = System.getProperty("user.dir") + "/db/hospital.csv";

    public HospitalDirectory() {
        loadHospitals();
    }

    public List<String> getCities() {
        return new ArrayList<>(cityHospitalMap.keySet());
    }

    public List<String> getHospitalsByCity(String city) {
        return cityHospitalMap.getOrDefault(city.toLowerCase(), new ArrayList<>());
    }

    private void loadHospitals() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No hospital data found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String city = parts[0].trim().toLowerCase();
                    String hospital = parts[1].trim();

                    cityHospitalMap.putIfAbsent(city, new ArrayList<>());
                    cityHospitalMap.get(city).add(hospital);
                }
            }
            System.out.println("[DEBUG] Loaded hospital data.");
        } catch (IOException e) {
            System.out.println("Error reading hospital file.");
        }
    }
}
