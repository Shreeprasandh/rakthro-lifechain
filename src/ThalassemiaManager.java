import java.io.*;
import java.util.*;

public class ThalassemiaManager {
    private static final String FILE_PATH = "db/thal_donors.csv";
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
    String donorId = sc.nextLine().trim();

    DonorManager dm = new DonorManager();
    Donor donor = dm.getDonorById(donorId);

    if (donor == null) {
        System.out.println("Donor ID not found!");
        return;
    }

    // 🏥 Hospital selection
    String selectedHospital = dm.selectHospital(sc);
    if (selectedHospital == null) {
        System.out.println("Hospital selection failed.");
        return;
    }

    String currentDate = java.time.LocalDate.now().toString();

    // 💌 Ask email manually
    System.out.print("Enter your email for confirmation: ");
    String email = sc.nextLine().trim();

    // ✏️ Save to thal_checkups.csv
    String dataLine = String.join(",",
        donor.getDonorId(),
        donor.getName(),
        email,
        donor.getCity(),
        donor.getBloodGroup(),
        currentDate,
        selectedHospital
    );

    // 💾 Write to file
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("db/thal_checkups.csv", true))) {
        bw.write(dataLine);
        bw.newLine();
    } catch (IOException e) {
        System.out.println("Error saving checkup info.");
        return;
    }

    // 💌 Send confirmation email
    try {
        String subject = "Thalassemia Checkup Appointment Confirmed!";
        String body = "Hi " + donor.getName() + ",\n\n"
                    + "Your Thalassemia checkup appointment has been successfully booked!\n\n"
                    + "Hospital: " + selectedHospital + "\n"
                    + "Date: " + currentDate + "\n\n"
                    + "We’ll update your thal status after the checkup.\n"
                    + "Thanks for your support \n\n– Rakthro Team";
        MailService.sendCustomEmail(email, subject, body);
    } catch (Exception e) {
        System.out.println("Could not send confirmation email.");
        e.printStackTrace(); // optional
    }

    System.out.println("Thalassemia checkup appointment booked successfully!");
}


    public void bookThalDonation(Scanner sc) {

    System.out.print("Enter your Donor ID: ");
    String id = sc.nextLine().trim();

    DonorManager dm = new DonorManager();
    Donor donor = dm.getDonorById(id);

    if (donor == null) {
        System.out.println("Donor ID not found!");
        return;
    }

    // Load thal donors
    List<ThalassemiaDonor> all = loadDonors();
    boolean success = false;

    for (ThalassemiaDonor d : all) {
        if (d.donorId.trim().equalsIgnoreCase(id.trim())) {

            if (!"Yes".equalsIgnoreCase(d.eligibleToDonateThal)) {
                System.out.println("You are not eligible to donate thalassemia-related blood.");
                return;
            }

            System.out.println("You are eligible to donate for: " + d.thalType.toUpperCase());

            // 🏥 Select hospital using the reusable method
            String selectedHospital = dm.selectHospital(sc);
            if (selectedHospital == null) {
                System.out.println("Hospital selection failed.");
                return;
            }

            // 📧 Ask for email confirmation
            System.out.print("Enter the email to send your donation appointment confirmation to: ");
            String enteredEmail = sc.nextLine().trim();
            d.email = enteredEmail;

            // 📅 Update donation date
            d.lastThalDonationDate = java.time.LocalDate.now().toString();
            saveDonors(all);

            // 📝 Log the donation (optional: pass selectedHospital to log method if needed)
            logThalDonation(d);

            // 💌 Send email
            try {
                String subject = "Thalassemia Donation Confirmed!";
                String body = "Hi " + d.name + ",\n\n"
                        + "Thank you for donating your precious " + d.thalType + " blood to help Thalassemia patients.\n"
                        + "Hospital: " + selectedHospital + "\n"
                        + "Donation Date: " + d.lastThalDonationDate + "\n\n"
                        + "You’re a real-life hero \n\n– Rakthro Team";
                MailService.sendCustomEmail(d.email, subject, body);
            } catch (Exception e) {
                System.out.println("Could not send confirmation email.");
            }

            success = true;
            break;
        }
    }

    if (success) {
        System.out.println("Donation booked successfully!");
    } else {
        System.out.println("Donor not registered as thalassemia donor yet.");
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

    public void cancelThalCheckup(Scanner sc) {
    System.out.print("Enter your Donor ID: ");
    String donorId = sc.nextLine().trim();

    List<String[]> allLines = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("db/thal_checkups.csv"))) {
        String line;
        int index = 0;

        System.out.println("\n--- Your Booked Checkups ---");
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", -1);
            if (parts.length >= 7 && parts[0].equalsIgnoreCase(donorId)) {
                System.out.println((indices.size() + 1) + ". " + parts[1] + " | " + parts[5] + " | " + parts[6]);
                indices.add(index); // mark this line
            }
            allLines.add(parts);
            index++;
        }
    } catch (IOException e) {
        System.out.println("Error reading checkup data.");
        return;
    }

    if (indices.isEmpty()) {
        System.out.println("No checkups found for this ID.");
        return;
    }

    System.out.print("Select appointment to cancel (1-" + indices.size() + "): ");
    int choice = Integer.parseInt(sc.nextLine().trim());

    if (choice < 1 || choice > indices.size()) {
        System.out.println("Invalid choice.");
        return;
    }

    int lineIndexToRemove = indices.get(choice - 1);
    String[] removedEntry = allLines.get(lineIndexToRemove);

    // Confirm deletion
    System.out.print("Are you sure you want to cancel this appointment? (yes/no): ");
    String confirm = sc.nextLine().trim();

    if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println("Cancellation aborted.");
        return;
    }

    // Remove the line
    allLines.remove(lineIndexToRemove);

    // Rewrite the file
    try (PrintWriter pw = new PrintWriter(new FileWriter("db/thal_checkups.csv"))) {
        for (String[] parts : allLines) {
            pw.println(String.join(",", parts));
        }
    } catch (IOException e) {
        System.out.println("Error saving file.");
        return;
    }

    // Email Notification
    try {
        String subject = "Thalassemia Checkup Appointment Cancelled";
        String body = "Hi " + removedEntry[1] + ",\n\n"
                    + "Your thalassemia checkup appointment on " + removedEntry[5] + " at " + removedEntry[6] + " has been cancelled as per your request.\n\n"
                    + "We hope to see you again soon!\n\n– Rakthro Team";
        MailService.sendCustomEmail(removedEntry[2], subject, body);
    } catch (Exception e) {
        System.out.println("Could not send cancellation email.");
    }

    System.out.println("Appointment cancelled successfully!");
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

    public void logThalDonation(ThalassemiaDonor d) {
    String filePath = "./db/thal_donations.csv";
    File file = new File(filePath);
    boolean isNewFile = !file.exists(); // ✅ this is optional now

    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
        // Write header only if new file
        if (isNewFile) {
            writer.println("ThalID,DonorID,Name,Email,City,Hospital,Date,Time");
        }

        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().toString();
        String hospital = d.checkupHospital == null || d.checkupHospital.equals("N/A") ? "N/A" : d.checkupHospital;

        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
            d.thalId,
            d.donorId,
            d.name,
            d.email,         //  This must be updated email from earlier step
            d.city,
            hospital,
            date,
            time
        );
    } catch (IOException e) {
        System.out.println("Error logging donation.");
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
    List<ThalassemiaDonor> all = loadDonors(); //  load existing data
    String thalId = generateThalId(all);       //  pass the list
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


public void cancelThalDonation(Scanner sc) {
    String filePath = "./db/thal_appointments.csv";
    File originalFile = new File(filePath);
    File tempFile = new File("./db/temp_thal_appointments.csv");

    // 1. Ask for Donor ID
    System.out.print("Enter your Donor ID: ");
    String donorId = sc.nextLine().trim();

    // 2. Read all appointments
    List<String[]> matchingAppointments = new ArrayList<>();
    List<String> allLines = new ArrayList<>();
    String email = null;

    try (BufferedReader reader = new BufferedReader(new FileReader(originalFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
            allLines.add(line); // store everything
            if (line.startsWith("ThalID")) continue; // skip header

            String[] parts = line.split(",", -1);
            if (parts.length >= 8 && parts[1].trim().equalsIgnoreCase(donorId)) {
                matchingAppointments.add(parts);
                email = parts[3].trim(); // get email
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading thal appointment data.");
        return;
    }

    // 3. If no appointments
    if (matchingAppointments.isEmpty()) {
        System.out.println("No thalassemia appointments found for this Donor ID.");
        return;
    }

    // 4. Display appointments
    System.out.println("\nYour Thalassemia Donation Appointments:");
    for (int i = 0; i < matchingAppointments.size(); i++) {
        String[] appt = matchingAppointments.get(i);
        System.out.printf("%d. ThalID: %s | Name: %s | Date: %s | Hospital: %s\n",
            i + 1, appt[0], appt[2], appt[6], appt[5]);
    }

    // 5. Select to cancel
    System.out.print("Select appointment to cancel (1-" + matchingAppointments.size() + "): ");
    int choice = Integer.parseInt(sc.nextLine().trim());

    if (choice < 1 || choice > matchingAppointments.size()) {
        System.out.println("Invalid selection.");
        return;
    }

    String[] selected = matchingAppointments.get(choice - 1);
    String selectedThalId = selected[0];
    String selectedDate = selected[6];
    String selectedHospital = selected[5];
    String selectedName = selected[2];

    // 6. Confirm
    System.out.print("Are you sure you want to cancel this appointment? (yes/no): ");
    String confirm = sc.nextLine().trim();
    if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println("Cancellation aborted.");
        return;
    }

    // 7. Write new data to temp file
    try (PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
        pw.println("ThalID,DonorID,Name,Email,City,Hospital,Date,Time"); // header again
        for (String[] parts : allLines.stream().map(l -> l.split(",", -1)).toList()) {
            if (parts.length < 8 || parts[0].equals(selectedThalId) && parts[6].equals(selectedDate)) {
                continue; // skip the one to cancel
            }
            pw.println(String.join(",", parts));
        }
    } catch (IOException e) {
        System.out.println("Error writing temp file.");
        return;
    }

    // 8. Replace original file
    if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
        System.out.println("File operation failed.");
        return;
    }

    // 9. Send email
    sendThalDonationCancelEmail(selectedName, email, selectedDate, selectedHospital);
    System.out.println("Donation appointment cancelled and email sent to: " + email);
}



public void viewAndRespondToRequests(Scanner sc) {
    String filePath = "./db/temp_thal_requests.csv";
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

    private void sendThalDonationCancelEmail(String name, String toEmail, String date, String hospital) {
    String subject = "Thalassemia Donation Cancelled";
    String body = String.format(
        "Hi %s,\n\nYour Thalassemia blood donation on %s at %s has been cancelled.\n\n" +
        "Thank you for your support.\n\n– Rakthro Team", name, date, hospital);
    MailService.sendCustomEmail(toEmail, subject, body);
}

    public void addNewDonorToThalDatabase(Donor donor) {
    List<ThalassemiaDonor> thalList = loadDonors();
    String thalId = generateThalId(thalList); // generate next ThalID

    ThalassemiaDonor newThalDonor = new ThalassemiaDonor(
        thalId,
        donor.getDonorId(),
        donor.getName(),
        donor.getAge(),
        donor.getBloodGroup(),
        donor.getCity(),
        donor.getContact(),
        donor.getEmail(),
        "N/A", // ThalType
        "Inactive", // ThalStatus
        "N/A", // LastCheckupDate
        "N/A", // LastThalDonationDate
        "No",  // EligibleToDonateThal
        "N/A", // CheckupHospital
        "N/A"  // ThalRemarks
    );

    thalList.add(newThalDonor);
    saveThalDonor(newThalDonor);
}

}