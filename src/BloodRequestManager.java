import java.io.*;
import java.util.*;

public class BloodRequestManager {
    private String filePath;  // Dynamic file path for each request type

    // Constructor to set which CSV file to work with
    public BloodRequestManager(String filePath) {
        this.filePath = filePath;
    }

    // Add a new blood request to the given CSV
    public void addRequest(BloodRequest req) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(req.toCSV());
            bw.newLine();
            System.out.println(" Request submitted.");
        } catch (IOException e) {
            System.out.println(" Failed to save request.");
        }
    }

    // Load all requests from the specified CSV
    public List<BloodRequest> getAllRequests() {
        List<BloodRequest> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                BloodRequest req = BloodRequest.fromCSV(line);
                if (req != null) list.add(req);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Remove request by email
    public void removeRequest(String email) {
        List<BloodRequest> list = getAllRequests();
        list.removeIf(r -> r.email.equalsIgnoreCase(email));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (BloodRequest r : list) {
                bw.write(r.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error removing request.");
        }
    }
}
