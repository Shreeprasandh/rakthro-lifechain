    import java.io.*;
    import java.util.*;

    public class SickleCellManager {
        private static final String DONOR_FILE = "db/sickle_donors.csv";
    private static final String TRANSFUSION_FILE = "db/sickle_transfusions.csv";

    public List<SickleCellDonor> loadSickleDonors() {
        List<SickleCellDonor> list = new ArrayList<>();
        File file = new File(DONOR_FILE);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                SickleCellDonor d = SickleCellDonor.fromCSV(line);
                if (d != null) list.add(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveSickleDonors(List<SickleCellDonor> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DONOR_FILE))) {
            for (SickleCellDonor d : list) {
                bw.write(d.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendSickleDonor(SickleCellDonor donor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DONOR_FILE, true))) {
            bw.write(donor.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateScId() {
        List<SickleCellDonor> list = loadSickleDonors();
        int max = 0;
        for (SickleCellDonor d : list) {
            String idNum = d.scdId.replaceAll("[^0-9]", "");
            int num = Integer.parseInt(idNum);
            if (num > max) max = num;
        }
        return "SC" + String.format("%03d", max + 1);
    }

    public void bookTransfusion(Scanner sc, HospitalDirectory dir) {
        System.out.print("Enter your Donor ID: ");
        String donorId = sc.nextLine();
        List<SickleCellDonor> list = loadSickleDonors();
        SickleCellDonor donor = list.stream().filter(d -> d.donorId.equals(donorId)).findFirst().orElse(null);

        if (donor == null) {
            System.out.println(" Donor not found.");
            return;
        }

        System.out.println("Available Cities:");
        List<String> cities = dir.getCities();
        for (int i = 0; i < cities.size(); i++) {
            System.out.println((i + 1) + ". " + cities.get(i));
        }
        System.out.print("Choose city number: ");
        int cityChoice = sc.nextInt(); sc.nextLine();
        String selectedCity = cities.get(cityChoice - 1);

        List<String> hospitals = dir.getHospitalsByCity(selectedCity);
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.println((i + 1) + ". " + hospitals.get(i));
        }
        System.out.print("Choose hospital number: ");
        int hospChoice = sc.nextInt(); sc.nextLine();
        String selectedHospital = hospitals.get(hospChoice - 1);

        System.out.print("Enter transfusion date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter time (HH:MM): ");
        String time = sc.nextLine();
        System.out.print("Enter your email to receive confirmation: ");
        String email = sc.nextLine();

        donor.lastTransfusionDate = date;
        donor.eligibleToDonateSCD = "No";
        saveSickleDonors(list); // Update the modified donor
        logTransfusion(donor, selectedCity, selectedHospital, date, time, email);
        MailService.sendSCDTransfusionConfirmation(email, donor.name, date, time, selectedHospital);
        System.out.println(" Transfusion booked & confirmation sent.");
    }

    public void logTransfusion(SickleCellDonor donor, String city, String hospital, String date, String time, String email) {
        String logEntry = donor.scdId + "," + donor.donorId + "," + donor.name + "," + city + "," + hospital + "," + date + "," + time + "," + email;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSFUSION_FILE, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSickleDashboard() {
        List<SickleCellDonor> donors = loadSickleDonors();

        if (donors.isEmpty()) {
            System.out.println(" No Sickle Cell donors found.");
            return;
        }

        System.out.println("\n---  Sickle Cell Donor Dashboard ---");
        System.out.printf("%-6s %-10s %-15s %-5s %-8s %-10s %-12s %-10s %-10s\n",
                        "SCDID", "DonorID", "Name", "Age", "Blood", "City", "LastTransfusion", "Eligible", "Status");

        for (SickleCellDonor d : donors) {
            System.out.printf("%-6s %-10s %-15s %-5d %-8s %-10s %-12s %-10s %-10s\n",
                            d.scdId, d.donorId, d.name, d.age, d.bloodGroup, d.city,
                            d.lastTransfusionDate, d.eligibleToDonateSCD, d.scdStatus);
        }
    }


        public void viewTransfusionHistory(String donorId) {
        List<String> records = new ArrayList<>();

        File file = new File("db/sickle_transfusions.csv");
        if (!file.exists()) {
            System.out.println(" No transfusion records found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[1].trim().equals(donorId)) {
                    records.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (records.isEmpty()) {
            System.out.println(" No transfusion history for Donor ID: " + donorId);
        } else {
            System.out.println("\n Transfusion History:");
            for (String rec : records) {
                System.out.println(rec);
            }
        }
    }


    public void cancelTransfusion(Scanner sc) {
        System.out.print("Enter your SCD ID to cancel transfusion: ");
        String scdId = sc.nextLine();

        File file = new File(TRANSFUSION_FILE);
        if (!file.exists()) {
            System.out.println("No transfusion records to cancel.");
            return;
        }

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(scdId + ",")) {
                    lines.add(line);
                } else {
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found) {
            System.out.println(" Transfusion appointment cancelled.");
        } else {
            System.out.println(" No matching appointment found.");
        }
    }

    public void appendDonorToFile(Donor donor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("donors.csv", true))) {
            bw.write(donor.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerSickleCheckup(Scanner sc, HospitalDirectory dir) {
    System.out.print("Enter your Donor ID: ");
    String donorId = sc.nextLine().trim();

    // Load existing SCD donors
    List<SickleCellDonor> list = loadSickleDonors();
    SickleCellDonor donor = list.stream()
        .filter(d -> d.donorId.equals(donorId))
        .findFirst()
        .orElse(null);

    if (donor == null) {
        System.out.println(" Donor not found.");
        return;
    }

    // Select city
    System.out.println("Available Cities:");
    List<String> cities = dir.getCities();
    for (int i = 0; i < cities.size(); i++) {
        System.out.println((i + 1) + ". " + cities.get(i));
    }
    System.out.print("Choose city number: ");
    int cityChoice = Integer.parseInt(sc.nextLine());
    String selectedCity = cities.get(cityChoice - 1);

    // Select hospital
    List<String> hospitals = dir.getHospitalsByCity(selectedCity);
    for (int i = 0; i < hospitals.size(); i++) {
        System.out.println((i + 1) + ". " + hospitals.get(i));
    }
    System.out.print("Choose hospital number: ");
    int hospChoice = Integer.parseInt(sc.nextLine());
    String selectedHospital = hospitals.get(hospChoice - 1);

    System.out.print("Enter checkup date (YYYY-MM-DD): ");
    String date = sc.nextLine();
    System.out.print("Enter checkup time (HH:MM): ");
    String time = sc.nextLine();
    System.out.print("Enter your email to receive confirmation: ");
    String email = sc.nextLine();

    // Save to checkup CSV
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("db/sickle_checkups.csv", true))) {
        String line = String.join(",", donor.donorId, donor.name, selectedCity, selectedHospital, date, time, email);
        bw.write(line);
        bw.newLine();
        System.out.println("Checkup booked!");
    } catch (IOException e) {
        System.out.println(" Error saving checkup.");
        e.printStackTrace();
    }

    // Send confirmation email
    MailService.sendCustomEmail(
        email,
        "Sickle Cell Checkup Confirmation - Rakthro",
        "Hello " + donor.name + ",\n\n" +
        "Your SCD checkup is confirmed:\n" +
        "📍 Hospital: " + selectedHospital + "\n" +
        "📅 Date: " + date + "\n" +
        "⏰ Time: " + time + "\n\n" +
        "Carry your Donor ID.\n\n– Rakthro Team"
    );
}

    public void cancelSickleCheckup(Scanner sc) {
    System.out.print("Enter your Donor ID to cancel checkup: ");
    String donorId = sc.nextLine().trim();

    File file = new File("db/sickle_checkups.csv");
    if (!file.exists()) {
        System.out.println(" No checkup records found.");
        return;
    }

    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 1 && parts[0].trim().equals(donorId)) {
                found = true;
                continue; // Skip the line (cancel it)
            }
            updatedLines.add(line);
        }
    } catch (IOException e) {
        System.out.println(" Error reading file.");
        return;
    }

    if (!found) {
        System.out.println(" No checkup found for given Donor ID.");
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        for (String updatedLine : updatedLines) {
            bw.write(updatedLine);
            bw.newLine();
        }
        System.out.println(" Checkup cancelled successfully.");
    } catch (IOException e) {
        System.out.println(" Error updating file.");
    }
}

    public void bookSickleDonation(Scanner sc, HospitalDirectory dir) {
    System.out.print("Enter your Donor ID: ");
    String donorId = sc.nextLine().trim();

    SickleCellDonor donor = getSickleDonorByDonorId(donorId);
    if (donor == null) {
        System.out.println(" Donor not found.");
        return;
    }

    System.out.println("Available Cities:");
    List<String> cities = dir.getCities();
    for (int i = 0; i < cities.size(); i++) {
        System.out.println((i + 1) + ". " + cities.get(i));
    }
    System.out.print("Choose city number: ");
    int cityChoice = Integer.parseInt(sc.nextLine());
    String city = cities.get(cityChoice - 1);

    List<String> hospitals = dir.getHospitalsByCity(city);
    for (int i = 0; i < hospitals.size(); i++) {
        System.out.println((i + 1) + ". " + hospitals.get(i));
    }
    System.out.print("Choose hospital number: ");
    int hospitalChoice = Integer.parseInt(sc.nextLine());
    String hospital = hospitals.get(hospitalChoice - 1);

    System.out.print("Enter appointment date (YYYY-MM-DD): ");
    String date = sc.nextLine();
    System.out.print("Enter time (HH:MM): ");
    String time = sc.nextLine();
    System.out.print("Enter your email to receive confirmation: ");
    String email = sc.nextLine();

    // Logging appointment
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("db/sickle_donations.csv", true))) {
        bw.write(donorId + "," + donor.name + "," + city + "," + hospital + "," + date + "," + time + "," + email);
        bw.newLine();
    } catch (IOException e) {
        System.out.println(" Error logging donation.");
    }

    MailService.sendSCDTransfusionConfirmation(email, donor.name, date, time, hospital);
    System.out.println(" Donation appointment booked & confirmation sent.");
}

    public SickleCellDonor getSickleDonorByDonorId(String donorId) {
    List<SickleCellDonor> list = loadSickleDonors();
    for (SickleCellDonor d : list) {
        if (d.donorId.equalsIgnoreCase(donorId)) {
            return d;
        }
    }
    return null;
}

    public void cancelSickleDonation(Scanner sc) {
    System.out.print("Enter your Donor ID to cancel donation: ");
    String donorId = sc.nextLine().trim();

    File file = new File("db/sickle_donations.csv");
    if (!file.exists()) {
        System.out.println(" No donation records found.");
        return;
    }

    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 1 && parts[0].equals(donorId)) {
                found = true;
                continue; // skip this entry
            }
            updatedLines.add(line);
        }
    } catch (IOException e) {
        System.out.println(" Error reading file.");
        return;
    }

    if (!found) {
        System.out.println(" No donation record found for this Donor ID.");
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        for (String updated : updatedLines) {
            bw.write(updated);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println(" Error updating file.");
        return;
    }

    System.out.println(" Donation appointment cancelled.");
}

    public void viewSickleDonationHistory(String donorId) {
    File file = new File("db/sickle_donations.csv");
    if (!file.exists()) {
        System.out.println("No donation history found.");
        return;
    }

    boolean found = false;
    System.out.println("\n--- Sickle Cell Donation History ---");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 6 && parts[0].equalsIgnoreCase(donorId)) {
                System.out.println("🏥 Hospital: " + parts[3]);
                System.out.println("📍 City: " + parts[2]);
                System.out.println("📅 Date: " + parts[4]);
                System.out.println("⏰ Time: " + parts[5]);
                System.out.println("------------------------");
                found = true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading donation history.");
    }

    if (!found) {
        System.out.println("No records found for Donor ID: " + donorId);
    }
}

}
