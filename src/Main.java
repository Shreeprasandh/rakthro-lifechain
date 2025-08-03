    import java.util.*;

    public class Main {
        public static void main(String[] args) {
            try (Scanner sc = new Scanner(System.in)) {
                DonorManager manager = new DonorManager();
                AuthSystem auth = new AuthSystem();
                HospitalDirectory hospitalDir = new HospitalDirectory();

                while (true) {
                    System.out.println("\n--- RAKTHRO Blood Donor Console ---");
                    System.out.println("1. Register Donor");
                    System.out.println("2. Search Donors");
                    System.out.println("3. Donation Appointment");
                    System.out.println("4. Donor Dashboard ");
                    System.out.println("5. Admin Login");
                    System.out.println("6. Exit");
                    System.out.print("Choose an option: ");
                    int choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.print("Name: ");
                            String name = sc.nextLine();
                            System.out.print("Age: ");
                            int age = sc.nextInt(); sc.nextLine();
                            System.out.print("Blood Group: ");
                            String bg = sc.nextLine();
                            System.out.print("City: ");
                            String city = sc.nextLine();
                            System.out.print("Contact: ");
                            String contact = sc.nextLine();
                            System.out.print("Last Donated Date (yyyy-mm-dd): ");
                            String lastDonatedDate = sc.nextLine();
                            String donorId = manager.generateDonorId(); //  Generate ID
                            Donor d = new Donor(donorId, name, age, bg, city, contact, lastDonatedDate);
                            manager.addDonor(d);
                            System.out.println("Donor Registered Successfully!");
                            break;

                        case 2:
                            System.out.print("Do you know the Donor ID? (yes/no): ");
                            String knowsId = sc.nextLine().trim().toLowerCase();
                            
                            if (knowsId.equals("yes")) {
                                System.out.print("Enter Donor ID: ");
                                donorId = sc.nextLine();
                                manager.searchById(donorId);
                            } else {
                                System.out.print("Enter Blood Group: ");
                                String sBg = sc.nextLine();
                                System.out.print("Enter City: ");
                                String sCity = sc.nextLine();
                                manager.searchDonors(sBg, sCity);
                            }
                            break;
                        case 3:
                                System.out.println("\n--- Donation Appointment ---");
                                System.out.println("1. Book New Appointment");
                                System.out.println("2. Cancel Appointment");
                                System.out.print("Choose an option: ");
                                int apptChoice = sc.nextInt(); sc.nextLine();
                                if (apptChoice == 1) {
                                    System.out.print("Enter your Donor ID: ");
                                    donorId = sc.nextLine();
                                    Donor donor = manager.getDonorById(donorId);

                                    if (donor == null) {
                                        System.out.println(" Donor ID not found.");
                                        break;
                                    }

                                    // ✅ Book appointment
                                    if (donor.getLastDonatedDate() == null ||
                                        donor.getLastDonatedDate().isBlank() ||
                                        donor.getLastDonatedDate().equalsIgnoreCase("N/A")) {
                                        System.out.println(" No last donation data available. Cannot book appointment.");
                                        break;
                                    }

                                    if (!manager.isEligibleToDonate(donor)) {
                                        int daysLeft = manager.daysUntilEligible(donor);
                                        System.out.println(" Not eligible to donate. Try again in " + daysLeft + " days.");
                                        break;
                                    }

                                    System.out.println("Available Cities: ");
                                    List<String> cities = hospitalDir.getCities();
                                    for (int i = 0; i < cities.size(); i++) {
                                        System.out.println((i + 1) + ". " + cities.get(i));
                                    }
                                    System.out.print("Choose city number: ");
                                    int cityChoice = sc.nextInt(); sc.nextLine();
                                    String selectedCity = cities.get(cityChoice - 1);

                                    List<String> hospitals = hospitalDir.getHospitalsByCity(selectedCity);
                                    if (hospitals.isEmpty()) {
                                        System.out.println("No hospitals found in this city.");
                                        break;
                                    }

                                    System.out.println("Available Hospitals in " + selectedCity + ":");
                                    for (int i = 0; i < hospitals.size(); i++) {
                                        System.out.println((i + 1) + ". " + hospitals.get(i));
                                    }
                                    System.out.print("Choose hospital number: ");
                                    int hospChoice = sc.nextInt(); sc.nextLine();
                                    String selectedHospital = hospitals.get(hospChoice - 1);

                                    manager.generateAppointmentSlip(donor, selectedCity, selectedHospital, sc);
                                }

                                else if (apptChoice == 2) {
                                    System.out.print("Enter your Donor ID to cancel appointment: ");
                                    String cancelId = sc.nextLine();
                                    manager.cancelAppointment(cancelId,sc);
                                }

                                break;

                        case 4:
                                manager.showDashboard(sc);  // <- Add this new method below in DonorManager.java
                                break;
                        case 5:
                                System.out.print("Username: ");
                                String u = sc.nextLine();
                                System.out.print("Password: ");
                                String p = sc.nextLine();
                                if (auth.login(u, p)) {
                                    System.out.println("Welcome Admin! ");

                                    while (true) {
                                        System.out.println("\n--- Admin Panel ---");
                                        System.out.println("1. View All Donors");
                                        System.out.println("2. Edit Donor");
                                        System.out.println("3. Delete Donor");
                                        System.out.println("4. View All Appointments"); 
                                        System.out.println("5. View Blood Requests"); 
                                        System.out.println("6. View Donors by Last Donation Date");
                                        System.out.println("7. Back to Main Menu");
                                        System.out.println("Choose an option: ");
                                        int adminChoice = sc.nextInt();
                                        sc.nextLine();

                                        switch (adminChoice) {
                                            case 1:
                                                manager.viewAllDonors();
                                                break;
                                            case 2:
                                                System.out.print("Enter Donor ID to edit: ");
                                                String editId = sc.nextLine().trim();
                                                manager.editDonor(editId, sc);
                                                break;
                                            case 3:
                                                System.out.print("Enter Donor ID to delete: ");
                                                String delId = sc.nextLine().trim();
                                                manager.deleteDonor(delId);
                                                break;
                                            case 4:
                                                manager.viewAllAppointments();
                                                break;
                                            case 5:
                                                manager.viewBloodRequests();
                                                break;
                                            case 6:
                                                manager.viewDonorsByDonationDate(); 
                                                break;
                                            case 7:
                                                System.out.println("Exiting Admin Panel...");
                                                break;
                                            default:
                                                System.out.println("Invalid option.");
                                        }

                                        if (adminChoice == 7) break;
                                    }
                                } else {
                                    System.out.println("Access Denied!");
                                }
                                break;
                        case 6:
                            System.out.println("Exiting RAKTHRO...");
                            return;
                        default:
                            System.out.println("Invalid option. Try again.");

                    }
                }
            }
        }
    }
