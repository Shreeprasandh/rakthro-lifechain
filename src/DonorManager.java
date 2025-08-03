import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;



public class DonorManager {
    List<Donor> donorList = new ArrayList<>();
    final String fileName = System.getProperty("user.dir") + "/db/donors.csv";



    public DonorManager() {
        loadFromFile();
    }

    public void addDonor(Donor d) {
    donorList.add(d);              
    saveToFile();         
}

    public String generateDonorId() {
    loadFromFile();  // Make sure we're using fresh file data
    int maxId = 0;
    for (Donor d : donorList) {
        try {
            int idNum = Integer.parseInt(d.donorId.substring(1)); // D001 -> 1
            if (idNum > maxId) maxId = idNum;
        } catch (Exception ignored) {}
    }
    return String.format("D%03d", maxId + 1);
}


    public void searchDonors(String bloodGroup, String city) {
        boolean found = false;
        for (Donor d : donorList) {
            if (d.bloodGroup.equalsIgnoreCase(bloodGroup) && d.city.equalsIgnoreCase(city)) {
                System.out.println(d);
                found = true;
            }
        }
        if (!found) System.out.println("No matching donors found.");
    }

    public void searchById(String donorId) {
    for (Donor d : donorList) {
        if (d.donorId.equalsIgnoreCase(donorId)) {
            System.out.println(d);
            return;
        }
    }
    System.out.println("No donor found with ID: " + donorId);
    }

    public void viewAllDonors() {
        if (donorList.isEmpty()) {
            System.out.println("No donors registered yet.");
        } else {
            for (Donor d : donorList) {
                System.out.println(d);
            }
        }
    }


    public void saveToFile() {
        File file = new File(fileName);
        try {
            file.getParentFile().mkdirs(); // ensure db/ folder exists
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("DonorID,Name,Age,BloodGroup,City,Contact,LastDonatedDate");

                for (Donor d : donorList) {
                    writer.println(d.toCSV());
                }
                System.out.println("[DEBUG] Saved " + donorList.size() + " donors to file: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error saving donor data.");
        }
    }
    public void showDashboard(Scanner sc) {
        if (donorList.isEmpty()) {
            System.out.println("No donors to show in dashboard.");
            return;
        }

        // STEP 1: City-wise counts
        Map<String, Integer> cityCount = new HashMap<>();
        for (Donor d : donorList) {
            cityCount.put(d.city, cityCount.getOrDefault(d.city, 0) + 1);
        }

        System.out.println("\n---  City-wise Donor Count ---");
        for (Map.Entry<String, Integer> entry : cityCount.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " donors");
        }

        System.out.print("\nEnter a city to explore: ");
        String city = sc.nextLine();

        // Filter donors in selected city
        List<Donor> cityDonors = new ArrayList<>();
        for (Donor d : donorList) {
            if (d.city.equalsIgnoreCase(city)) {
                cityDonors.add(d);
            }
        }

        if (cityDonors.isEmpty()) {
            System.out.println("No donors found in that city.");
            return;
        }

        // STEP 2: Blood group count in that city
        Map<String, Integer> bgCount = new HashMap<>();
        for (Donor d : cityDonors) {
            bgCount.put(d.bloodGroup, bgCount.getOrDefault(d.bloodGroup, 0) + 1);
        }

        System.out.println("\n---  Blood Group Count in " + city + " ---");
        for (Map.Entry<String, Integer> entry : bgCount.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue() + " donors");
        }

        System.out.print("\nEnter a blood group to view full donor details: ");
        String bg = sc.nextLine();

        // STEP 3: Show final matching donors
        System.out.println("\n--- Donors in " + city + " with BG: " + bg + " ---");
        boolean found = false;
        for (Donor d : cityDonors) {
            if (d.bloodGroup.equalsIgnoreCase(bg)) {
                System.out.println(d);
                found = true;
            }
        }

        if (!found) System.out.println("No matching donors found.");
    }   

        public void editDonor(String id, Scanner sc) {
        Donor donor = null;
        for (Donor d : donorList) {
            if (d.donorId.equalsIgnoreCase(id)) {
                donor = d;
                break;
            }
        }
        if (donor == null) {
            System.out.println("Donor ID not found.");
            return;
        }

        System.out.println("Editing Donor: " + donor.name);
        System.out.print("New City (Leave blank to keep current): ");
        String city = sc.nextLine();
        if (!city.trim().isEmpty()) donor.city = city;

        System.out.print("New Contact (Leave blank to keep current): ");
        String contact = sc.nextLine();
        if (!contact.trim().isEmpty()) donor.contact = contact;

        saveToFile();
        System.out.println("Donor updated successfully!");
    }
    public Donor getDonorById(String id) {
    for (Donor d : donorList) {
        if (d.donorId.equalsIgnoreCase(id.trim())) return d;
    }
    return null;
}

    public boolean isEligibleToDonate(Donor donor) {
    if (donor.lastDonatedDate == null || donor.lastDonatedDate.equalsIgnoreCase("N/A") || donor.lastDonatedDate.isBlank()) {
        return true;
    }
    LocalDate lastDate = LocalDate.parse(donor.lastDonatedDate);
    return ChronoUnit.DAYS.between(lastDate, LocalDate.now()) >= 90;
}



    public int daysUntilEligible(Donor donor) {
    if (donor.lastDonatedDate == null || donor.lastDonatedDate.equalsIgnoreCase("N/A") || donor.lastDonatedDate.isBlank()) {
        return 0;
    }
    LocalDate lastDate = LocalDate.parse(donor.lastDonatedDate);
    long days = ChronoUnit.DAYS.between(lastDate, LocalDate.now());
    return 90 - (int) days;
}


    public void generateAppointmentSlip(Donor donor, String city, String hospital, Scanner sc) {
    LocalDate appointmentDate = LocalDate.now().plusDays(new Random().nextInt(7) + 1); // 1-7 days later
    String time = (new Random().nextInt(4) + 9) + ":00 AM"; // 9 to 12 AM

    String fileName = "./appointments/" + donor.donorId + "_appointment.txt";
    File file = new File(fileName);
    file.getParentFile().mkdirs();

    try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
        writer.println("RAKTHRO - Blood Donation Appointment");
        writer.println("------------------------------------");
        writer.println("Donor ID: " + donor.donorId);
        writer.println("Name    : " + donor.name);
        writer.println("Blood   : " + donor.bloodGroup);
        writer.println("City    : " + city);
        writer.println("Hospital: " + hospital);
        writer.println("Date    : " + appointmentDate);
        writer.println("Time    : " + time);
        writer.println("------------------------------------");
        writer.println("Please bring a valid ID and arrive 15 mins early.");
    } catch (IOException e) {
        System.out.println(" Failed to generate appointment slip.");
    }

    //  Ask for temporary email at runtime (won't be stored)
    System.out.print(" Enter your email to receive confirmation: ");
    String tempEmail = sc.nextLine();

    //  Send email using MailService
    try {
        MailService.sendConfirmationEmail(
            tempEmail,
            donor.getName(),
            appointmentDate.toString(),
            time,
            hospital
        );
        System.out.println(" Email confirmation sent to your email ");
    } catch (Exception e) {
        System.out.println(" Failed to send email confirmation.");
        e.printStackTrace(); // optional for debugging
    }
}

    public void logAppointment(Donor donor, String email, String city, String hospital, String date, String time) {
    String filePath = "./db/appointments.csv";
    File file = new File(filePath);
    file.getParentFile().mkdirs(); // ensure db folder

    boolean newFile = !file.exists();

    try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
        if (newFile) {
            writer.println("DonorID,Name,Email,BloodGroup,City,Hospital,Date,Time");
        }
        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
                donor.donorId,
                donor.name,
                email,
                donor.bloodGroup,
                city,
                hospital,
                date,
                time
        );
    } catch (IOException e) {
        System.out.println(" Failed to log appointment.");
    }
}

    
    public void deleteDonor(String id) {
    Iterator<Donor> iterator = donorList.iterator();
    while (iterator.hasNext()) {
        Donor d = iterator.next();
        if (d.donorId.equalsIgnoreCase(id)) {
            iterator.remove();
            saveToFile();
            System.out.println("Donor " + d.name + " has been removed.");
            return;
        }
    }
    System.out.println("Donor ID not found.");
}


    public void loadFromFile() {
    donorList.clear(); // ✅ VERY IMPORTANT

    File file = new File(fileName);
    if (!file.exists()) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Could not create donor file.");
            return;
        }
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        reader.readLine(); // Skip header
        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            Donor donor = Donor.fromCSV(line);
            if (donor != null) donorList.add(donor);
        }
        System.out.println("[DEBUG] Loaded " + donorList.size() + " donors from file: " + fileName);
    } catch (IOException e) {
        System.out.println("Error loading donor data.");
    }
}

}

