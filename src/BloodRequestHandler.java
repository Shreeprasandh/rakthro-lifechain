import java.util.*;

public class BloodRequestHandler {

    private static final String NORMAL_PATH = "db/blood_requests.csv";
    private static final String THAL_PATH = "db/thal_blood_requests.csv";
    private static final String SICKLE_PATH = "db/sickle_blood_requests.csv";

    public static void handleRequestMenu(Scanner sc, DonorManager manager, HospitalDirectory hospitalDir, String type) {
        BloodRequestManager brm;
        String title;
        String filePath;

        switch (type.toLowerCase()) {
            case "thal":
                filePath = THAL_PATH;
                title = "Thalassemia Blood Request Portal";
                break;
            case "sickle":
                filePath = SICKLE_PATH;
                title = "Sickle Cell Blood Request Portal";
                break;
            default:
                filePath = NORMAL_PATH;
                title = "Normal Blood Request Portal";
                break;
        }

        brm = new BloodRequestManager(filePath);

        while (true) {
            System.out.println("\n--- " + title + " ---");
            System.out.println("1. Book Blood Request");
            System.out.println("2. View & Fulfil Requests");
            System.out.println("3. Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("City: ");
                String city = sc.nextLine();
                System.out.print("Blood Group: ");
                String bg = sc.nextLine();
                System.out.print("Contact: ");
                String contact = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();

                BloodRequest req = new BloodRequest(name, city, bg, contact, email);

                System.out.print("Confirm submission? (yes/no): ");
                String confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("yes")) {
                    brm.addRequest(req);
                    System.out.println(" Blood request submitted successfully!");
                } else {
                    System.out.println("Cancelled.");
                }

            } else if (choice.equals("2")) {
                List<BloodRequest> requests = brm.getAllRequests();
                if (requests.isEmpty()) {
                    System.out.println("No requests found.");
                    continue;
                }

                System.out.println("--- Pending Requests ---");
                for (int i = 0; i < requests.size(); i++) {
                    BloodRequest r = requests.get(i);
                    System.out.println((i + 1) + ". " + r.name + " | " + r.bloodGroup + " | " + r.city + " | " + r.email);
                }

                System.out.print("Enter your Donor ID: ");
                String donorId = sc.nextLine();
                Donor donor = manager.getDonorById(donorId);

                if (donor == null) {
                    System.out.println(" Donor not found.");
                    continue;
                }

                if (type.equals("thal") || type.equals("sickle")) {
                    if (!manager.isEligibleToDonate(donor)) {
                        int daysLeft = manager.daysUntilEligible(donor);
                        System.out.println(" Not eligible to donate. Try again in " + daysLeft + " days.");
                        continue;
                    }
                }

                System.out.print("Enter request number to fulfil or 0 to cancel: ");
                int index;
                try {
                    index = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println(" Invalid input.");
                    continue;
                }

                if (index == 0) continue;
                if (index < 1 || index > requests.size()) {
                    System.out.println(" Invalid request number.");
                    continue;
                }

                BloodRequest selectedReq = requests.get(index - 1);

                String hospital = manager.selectHospital(sc);
                if (hospital == null) {
                    System.out.println(" Hospital selection cancelled.");
                    continue;
                }


                System.out.print("Enter appointment date (YYYY-MM-DD): ");
                String date = sc.nextLine();
                System.out.print("Enter time (HH:MM): ");
                String time = sc.nextLine();

                // Use donor's registered email for confirmation
                String email = donor.getEmail();

                manager.generateAppointmentSlip(donor, donor.city, hospital, sc);
                manager.logAppointment(donor,email, donor.city, hospital, date, time);

                
                /*MailService.sendConfirmationEmail(email, donor.getName(), date, time, hospital);*/

                brm.removeRequest(selectedReq.email);
                System.out.println(" Request fulfilled and appointment booked successfully.");

            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println(" Invalid choice. Please try again.");
            }
        }
    }
}
