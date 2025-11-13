    import java.util.*;
    public class Main {
        public static void main(String[] args) {
            try (Scanner sc = new Scanner(System.in)) {
                DonorManager manager = new DonorManager();
                BloodRequestHandler requestHandler = new BloodRequestHandler();
                HospitalDirectory hospitalDir = new HospitalDirectory();
                AuthSystem auth = new AuthSystem();

                System.out.println("Welcome to RAKTHRO Blood Donor Management System");
                while (true) {
                    System.out.println("1. Admin Login");
                    System.out.println("2. User Login");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");
                    int loginChoice = sc.nextInt();
                    sc.nextLine();

                    if (loginChoice == 1) {
                        System.out.print("Username: ");
                        String u = sc.nextLine();
                        System.out.print("Password: ");
                        String p = sc.nextLine();

                        if (auth.login(u, p)) {
                            System.out.println("Welcome Admin!");

                            while (true) {
                                System.out.println("\n--- Admin Panel ---");
                                System.out.println("1. View All Donors");
                                System.out.println("2. Search Donors");
                                System.out.println("3. Edit Donor");
                                System.out.println("4. Delete Donor");
                                System.out.println("5. View All Appointments");
                                System.out.println("6. View Blood Requests");
                                System.out.println("7. View Donors by Last Donation Date");
                                System.out.println("8. Logout");
                                System.out.print("Choose an option: ");
                                int adminChoice = sc.nextInt();
                                sc.nextLine();

                                switch (adminChoice) {
                                    case 1:
                                        manager.viewAllDonors();
                                        break;
                                    case 2:
                                        System.out.print("Do you know the Donor ID? (yes/no): ");
                                        String knowsId = sc.nextLine().trim().toLowerCase();

                                        if (knowsId.equals("yes")) {
                                            System.out.print("Enter Donor ID: ");
                                            String donorId = sc.nextLine();
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
                                        System.out.print("Enter Donor ID to edit: ");
                                        String editId = sc.nextLine().trim();
                                        manager.editDonor(editId, sc);
                                        break;
                                    case 4:
                                        System.out.print("Enter Donor ID to delete: ");
                                        String delId = sc.nextLine().trim();
                                        manager.deleteDonor(delId);
                                        break;
                                    case 5:
                                        manager.viewAllAppointments();
                                        break;
                                    case 6:
                                        requestHandler.viewBloodRequests();
                                        break;
                                    case 7:
                                        manager.viewDonorsByDonationDate();
                                        break;
                                    case 8:
                                        System.out.println("Exiting Admin Panel...");
                                        break;
                                    default:
                                        System.out.println("Invalid option.");
                                }

                                if (adminChoice == 8) {
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Access Denied!");
                        }
                    } 
                    else if (loginChoice == 2) {
                        System.out.println("1. Log In");
                        System.out.println("2. Sign In");
                        System.out.print("Choose an option: ");
                        int userChoice = sc.nextInt();
                        sc.nextLine();

                        if (userChoice == 1) {
                            System.out.print("Do you know your Donor ID? (yes/no): ");
                            String knowsId = sc.nextLine().trim().toLowerCase();

                            if (knowsId.equals("yes")) {
                                System.out.print("Donor ID: ");
                                String donorId = sc.nextLine();
                                System.out.print("Password: ");
                                String password = sc.nextLine();

                                if (auth.userLoginByDonorId(donorId, password)) {
                                    System.out.println("Login Successful!");
                                } else {
                                    System.out.println("Invalid credentials.");
                                    continue;
                                }
                            } else {
                                System.out.print("Username: ");
                                String username = sc.nextLine();
                                System.out.print("Email: ");
                                String email = sc.nextLine();
                                System.out.print("Password: ");
                                String password = sc.nextLine();

                                if (auth.userLoginByCredentials(username, email, password)) {
                                    System.out.println("Login Successful!");
                                } else {
                                    System.out.println("Invalid credentials.");
                                    continue;
                                }
                            }
                        } else if (userChoice == 2) {
                            Donor newDonor = manager.registerNewDonor(sc);
                            if (newDonor != null) {
                                System.out.print("Password: ");
                                String password = sc.nextLine();
                                manager.addUserToCSV(newDonor.donorId, newDonor.name, newDonor.email, password);
                                System.out.println("Sign In Successful! You can now log in.");
                            }
                            continue;
                        } else {
                            System.out.println("Invalid option.");
                            continue;
                        }

                        // if login successful, enter the while loop
                        while (true) {
                            System.out.println("\n--- RAKTHRO Blood Donor Console ---");
                            System.out.println("1. Register Donor");
                            System.out.println("2. Donation Appointment");
                            System.out.println("3. Donor Dashboard");
                            System.out.println("4. Disease-Based Blood Donation");
                            System.out.println("5. Blood Request");
                            System.out.println("6. Logout");
                            System.out.print("Choose an option: ");
                            int userchoice = sc.nextInt();
                            sc.nextLine();

                            switch (userchoice) {
                                case 1:
                                    System.out.println("You are already a donor.");
                                    System.out.println("Want to register new donor? (yes/no)");
                                    String choice = sc.nextLine().trim().toLowerCase();
                                    System.out.println("logging out....");
                                    if (choice.equals("yes")) {
                                        Donor newDonor = manager.registerNewDonor(sc);
                                        if (newDonor != null) {
                                            System.out.print("Password: ");
                                            String password = sc.nextLine();
                                            manager.addUserToCSV(newDonor.donorId, newDonor.name, newDonor.email, password);
                                            System.out.println("Sign In Successful! You can now log in.");
                                        }
                                    } else {
                                        System.out.println("Returning to main menu.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("\n--- Donation Appointment ---");
                                    System.out.println("1. Book New Appointment");
                                    System.out.println("2. Cancel Appointment");
                                    System.out.println("3. Back to Main Menu");
                                    System.out.print("Choose an option: ");
                                    int apptChoice = sc.nextInt();
                                    sc.nextLine();
                                    if (apptChoice == 1) {
                                        System.out.print("Enter your Donor ID: ");
                                        String donorId = sc.nextLine();
                                        Donor donor = manager.getDonorById(donorId);

                                        if (donor == null) {
                                            System.out.println("Donor ID not found.");
                                            break;
                                        }

                                        // Book appointment
                                        if (donor.getLastDonatedDate() == null ||
                                            donor.getLastDonatedDate().isBlank() ||
                                            donor.getLastDonatedDate().equalsIgnoreCase("N/A")) {
                                            System.out.println("No last donation data available. Cannot book appointment.");
                                            break;
                                        }

                                        if (!manager.isEligibleToDonate(donor)) {
                                            int daysLeft = manager.daysUntilEligible(donor);
                                            System.out.println("Not eligible to donate. Try again in " + daysLeft + " days.");
                                            break;
                                        }

                                        // Use new selectHospital
                                        String selectedHospital = manager.selectHospital(sc);
                                        if (selectedHospital == null) {
                                            System.out.println("Hospital selection failed.");
                                            break;
                                        }

                                        // Pass donor.city as city for slip
                                        manager.generateAppointmentSlip(donor, donor.city, selectedHospital, sc);
                                    } else if (apptChoice == 2) {
                                        System.out.print("Enter your Donor ID to cancel appointment: ");
                                        String cancelId = sc.nextLine();
                                        manager.cancelAppointment(cancelId, sc);
                                    } else if (apptChoice == 3) {
                                        break; // back to main menu
                                    } else {
                                        System.out.println("Invalid option.");
                                    }
                                    break;
                                case 3:
                                    manager.showDashboard(sc); // <- Add this new method below in DonorManager.java
                                    break;
                                case 4:
                                    DiseaseDonationManager.showDiseaseBasedDonationMenu(sc, manager);
                                    break;
                                case 5:
                                    BloodRequestHandler.handleRequestMenu(sc, manager, hospitalDir, "normal");
                                    break;
                                case 6:
                                        System.out.println("Exiting User Panel...");
                                        break;
                                default:
                                    System.out.println("Invalid option. Try again.");
                            }
                            if (userchoice == 6) {
                                    break;
                                }
                        }
                    }
                    else if (loginChoice == 3) {
                        System.out.println("Exiting RAKTHRO System.");
                        break;
                    }
                    else {
                        System.out.println("Invalid option. Try again.");
                    }
                }
            }
        }
    }
            
    
