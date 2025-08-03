import java.io.*;
import java.util.*;

public class ThalassemiaManager {
    private static final String FILE_PATH = "./db/thal_donors.csv";
    public List<ThalassemiaDonor> loadDonors() {
    List<ThalassemiaDonor> list = new ArrayList<>();
    File file = new File("db/thal_donors.csv");

    if (!file.exists()) return list;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line = br.readLine(); // Skip header
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                ThalassemiaDonor d = ThalassemiaDonor.fromCSV(line);
                if (d != null) list.add(d);
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading thal donors.");
    }

    return list;
}

    public void saveDonors(List<ThalassemiaDonor> donors) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
        pw.println("ThalID,DonorID,Name,Age,City,Contact,Email,ThalType,ThalStatus,LastCheckupDate,LastThalDonationDate,EligibleToDonateThal,CheckupHospital,ThalRemarks");
        for (ThalassemiaDonor d : donors) {
            pw.println(d.toCSV());
        }
    } catch (IOException e) {
        System.out.println("Failed to save thalassemia donor data.");
    }
}



    public ThalassemiaDonor findById(String donorId) {
        for (ThalassemiaDonor d : loadDonors()) {
            if (d.donorId.equalsIgnoreCase(donorId)) return d;
        }
        return null;
    }


    public String generateThalId(List<ThalassemiaDonor> thal_Donors) {
    int max = 0;
    for (ThalassemiaDonor d : thal_Donors) {
        if (d.thalId != null && d.thalId.startsWith("T")) {
            try {
                int num = Integer.parseInt(d.thalId.substring(1));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }
    }
    return "T" + String.format("%03d", max + 1);
}


    public void registerCheckup(Scanner sc) {
        System.out.print("Enter your Donor ID: ");
        String id = sc.nextLine().trim();
        List<ThalassemiaDonor> all = loadDonors();
        boolean updated = false;

        for (ThalassemiaDonor d : all) {
            if (d.donorId.equalsIgnoreCase(id)) {
                System.out.print("Enter Thalassemia Type (Alpha/Beta): ");
                d.thalType = sc.nextLine().trim();
                System.out.print("Enter Thal Status (Carrier/Affected/Normal): ");
                d.thalStatus = sc.nextLine().trim();
                System.out.print("Enter Checkup Hospital Name: ");
                d.checkupHospital = sc.nextLine().trim();
                System.out.print("Enter any remarks: ");
                d.thalRemarks = sc.nextLine().trim();
                d.lastCheckupDate = java.time.LocalDate.now().toString();
                d.eligibleToDonateThal = d.thalStatus.equalsIgnoreCase("Carrier") ? "Yes" : "No";
                updated = true;
                break;
            }
        }

        if (updated) {
    saveDonors(all);

    try {
        ThalassemiaDonor donor = findById(id); //  Get updated donor info
        String subject = "🩺 Thalassemia Checkup Registered!";
        String body = "Hi " + donor.name + ",\n\nYour Thalassemia checkup has been successfully recorded.\n\n"
                    + "Type: " + donor.thalType + "\n"
                    + "Status: " + donor.thalStatus + "\n"
                    + "Hospital: " + donor.checkupHospital + "\n\n"
                    + "You're marked as " + (donor.eligibleToDonateThal.equalsIgnoreCase("Yes") ? "eligible" : "not eligible") + " to donate special blood.\n\n"
                    + "– Rakthro Team 💉";

        MailService.sendCustomEmail(donor.email, subject, body);
    } catch (Exception e) {
        System.out.println("Couldn't send confirmation email ");
    }

    System.out.println("Checkup data updated successfully.");
}

    }

    public void bookThalDonation(Scanner sc) {
    System.out.print("Enter your Donor ID: ");
    String id = sc.nextLine().trim();

    List<ThalassemiaDonor> all = loadDonors();
    boolean success = false;

    for (ThalassemiaDonor d : all) {
        if (d.donorId.equalsIgnoreCase(id)) {

            if (!"Yes".equalsIgnoreCase(d.eligibleToDonateThal)) {
                System.out.println("You are not eligible to donate thalassemia-related blood.");
                return;
            }

            System.out.println("You are eligible to donate for: " + d.thalType.toUpperCase());
            System.out.print("Confirm donation for " + d.thalType + "? (yes/no): ");
            String confirm = sc.nextLine().trim();

            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Donation cancelled.");
                return;
            }

            d.lastThalDonationDate = java.time.LocalDate.now().toString();
            saveDonors(all); // save updates to thal_donors.csv

            logThalDonation(d); //  LOG IT into thal_donations.csv

            success = true;

            try {
                String subject = "Thalassemia Donation Confirmed!";
                String body = "Hi " + d.name + ",\n\nThank you for donating your precious " + d.thalType + " blood to help Thalassemia patients.\n"
                            + "Donation Date: " + d.lastThalDonationDate + "\n\n"
                            + "You’re a real-life hero!\n\n– Rakthro Team";
                MailService.sendCustomEmail(d.email, subject, body);
            } catch (Exception e) {
                System.out.println("Could not send confirmation email.");
            }

            break;
        }
    }

    if (success) {
        System.out.println("Donation booked successfully!");
    } else {
        System.out.println("Donor not found.");
    }
}


    public void saveThalDonor(ThalassemiaDonor donor) {
    String filePath = "db/thal_donors.csv";
    File file = new File(filePath);
    boolean isNewFile = !file.exists();

    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
        if (isNewFile) {
            writer.println("ThalID,DonorID,Name,Age,BloodGroup,City,Contact,Email,ThalType,ThalStatus,LastCheckupDate,LastThalDonationDate,EligibleToDonateThal,CheckupHospital,ThalRemarks");
        }
        writer.printf("%s,%s,%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
            donor.thalId,
            donor.donorId,
            donor.name,
            donor.age,
            donor.bloodGroup,   
            donor.city,
            donor.contact,
            donor.email,
            donor.thalType,
            donor.thalStatus,
            donor.lastCheckupDate,
            donor.lastThalDonationDate,
            donor.eligibleToDonateThal,
            donor.checkupHospital,
            donor.thalRemarks
        );
    } catch (IOException e) {
        System.out.println(" Error saving Thalassemia donor.");
        e.printStackTrace();
    }

}


    public void viewThalDashboard() {
        List<ThalassemiaDonor> all = loadDonors();
        Map<String, Integer> cityCount = new HashMap<>();
        Map<String, Integer> typeCount = new HashMap<>();

        for (ThalassemiaDonor d : all) {
            if (d.eligibleToDonateThal.equalsIgnoreCase("Yes")) {
                cityCount.put(d.city, cityCount.getOrDefault(d.city, 0) + 1);
                typeCount.put(d.thalType, typeCount.getOrDefault(d.thalType, 0) + 1);
            }
        }

        System.out.println("\n Thalassemia Dashboard:");
        System.out.println(" Affected Donors by City:");
        for (String city : cityCount.keySet()) {
            System.out.println("- " + city + ": " + cityCount.get(city));
        }

        System.out.println("\n Donor Types:");
        for (String type : typeCount.keySet()) {
            System.out.println("- " + type + ": " + typeCount.get(type));
        }
    }

    public void logThalDonation(ThalassemiaDonor donor) {
    String filePath = "./db/thal_donations.csv";

    try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
        String date = java.time.LocalDate.now().toString();
        pw.println(donor.donorId + "," + donor.name + "," + donor.thalType + "," + date);
    } catch (IOException e) {
        System.out.println("Error logging thalassemia donation.");
    }
}

    public void viewThalDonationHistory(String donorId) {
    String filePath = "./db/thal_donations.csv";
    boolean found = false;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        System.out.println("\n--- Your Thalassemia Donation History ---");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase(donorId)) {
                System.out.println("Date: " + parts[3] + " | Type: " + parts[2]);
                found = true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading donation log.");
    }

    if (!found) {
        System.out.println("No donation history found.");
    }
}

    public void registerThalassemiaUser(Scanner sc, DonorManager donorManager) {
    System.out.print("Do you already have a Donor ID? (yes/no): ");
    String hasId = sc.nextLine().trim();

    Donor baseDonor = null;

    if (hasId.equalsIgnoreCase("yes")) {
        System.out.print("Enter your Donor ID: ");
        String donorId = sc.nextLine().trim();
        baseDonor = donorManager.getDonorById(donorId);
        if (baseDonor == null) {
            System.out.println("Donor not found.");
            return;
        }   
    } else {
        donorManager.registerNewDonor(sc);
        System.out.print("Enter the new Donor ID generated: ");
        String donorId = sc.nextLine().trim();
        baseDonor = donorManager.getDonorById(donorId);
        if (baseDonor == null) {
            System.out.println("Could not retrieve donor.");
            return;
        }
    }

    // Proceed to register in Thalassemia
    List<ThalassemiaDonor> all = loadDonors(); // 🔥 load existing data
    String thalId = generateThalId(all);       // ✅ pass the list
    ThalassemiaDonor thalDonor = new ThalassemiaDonor(
    thalId,
    baseDonor.getDonorId(),
    baseDonor.getName(),
    baseDonor.getAge(),
    baseDonor.getBloodGroup(),
    baseDonor.getCity(),
    baseDonor.getContact(),
    baseDonor.getEmail(),
    "N/A", "N/A", "N/A", "N/A", "No", "N/A", "N/A"
);


    saveThalDonor(thalDonor);

    System.out.println("Registered as Thalassemia donor. Your Thal ID: " + thalDonor.thalId);
}


public void viewAndRespondToRequests(Scanner sc) {
    String filePath = "./db/thal_requests.csv";
    List<String> allLines = new ArrayList<>();

    System.out.print("Enter your Donor ID: ");
    String donorId = sc.nextLine().trim();
    ThalassemiaDonor donor = findById(donorId);

    if (donor == null || !"Yes".equalsIgnoreCase(donor.eligibleToDonateThal)) {
        System.out.println("You are not eligible to view/respond to requests.");
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        int index = 1;
        System.out.println("\n--- Open Requests Matching Your Thal Type ---");

        while ((line = br.readLine()) != null) {
            if (line.startsWith("RequestID")) {
                allLines.add(line); // keep header
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length >= 6) {
                String requestId = parts[0].trim();
                String name = parts[1].trim();
                String city = parts[2].trim();
                String requiredType = parts[3].trim();
                String status = parts[5].trim();

                if (status.equalsIgnoreCase("Open") && requiredType.equalsIgnoreCase(donor.thalType)) {
                    System.out.println(index + ". " + requestId + " | " + name + " | " + city + " | Type: " + requiredType);
                    index++;
                }

                allLines.add(line); // store for later
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading requests.");
        return;
    }

    System.out.print("Enter Request ID to accept or 'back': ");
    String choice = sc.nextLine().trim();
    if (choice.equalsIgnoreCase("back")) return;

    try (PrintWriter pw = new PrintWriter(new FileWriter("./db/temp_thal_requests.csv"))) {


        for (String line : allLines) {
            if (line.startsWith("RequestID")) {
                pw.println(line);
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length >= 6 && parts[0].trim().equalsIgnoreCase(choice)) {
                parts[5] = "Accepted by " + donor.name; // change status
                System.out.println(" You’ve accepted the donation request.");
                String subject = "Thal Donation Match Found!";
                String body = "Hi " + donor.name + ",\n\nYou have accepted a donation request for " + parts[1] + " in " + parts[2] + ".\n"
                            + "Thank you for stepping up \n\n– Rakthro Team";
                try {
                    MailService.sendCustomEmail(donor.email, subject, body);
                } catch (Exception e) {
                    System.out.println("Could not send email.");
                }
                pw.println(String.join(",", parts));
            } else {
                pw.println(line);
            }
        }

        // Replace original file
        File original = new File(filePath);
        File temp = new File("./db/temp_thal_requests.csv");
        if (original.delete()) temp.renameTo(original);
    } catch (IOException e) {
        System.out.println("Error updating request.");
    }
}

}