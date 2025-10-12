import java.util.List;
import java.util.Scanner;

public class DiseaseDonationManager {

    public static void showDiseaseBasedDonationMenu(Scanner sc, DonorManager manager) {
        ThalassemiaManager thal = new ThalassemiaManager();;
        SickleCellManager scdManager = new SickleCellManager();
        HospitalDirectory hospitalDir = new HospitalDirectory();


        while (true) {
            System.out.println("\n--- Disease-Based Donation System  ---");
            System.out.println("1. Thalassemia");
            System.out.println("2. Sickle Cell Anemia (not added yet)");
            System.out.println("3. Myelodysplastic Syndromes (not added yet)");
            System.out.println("4. Hemophilia (not added yet)");
            System.out.println("5. Back");

            System.out.print("Choose a category: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    showThalassemiaMenu(sc, manager, thal);
                    break;
                case "2":
                    showSickleCellMenu(sc, manager, scdManager, hospitalDir);
                    break;
                case "3":
                    System.out.println("Myelodysplastic Syndromes is not implemented yet.");
                    break;
                case "4":
                    System.out.println("Hemophilia Assistance is not implemented yet.");
                    break;
                case "5":
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void showThalassemiaMenu(Scanner sc, DonorManager manager, ThalassemiaManager thal) {
        while (true) {
            System.out.println("\n--- Thalassemia Support ---");
            System.out.println("1. Register as Donor for Thalassemia");
            System.out.println("2. Thalassemia Checkup");
            System.out.println("3. Thalassemia Blood Donation");
            System.out.println("4. View Thalassemia Dashboard");
            System.out.println("5. View My Donation History");
            System.out.println("6. View & Respond to Thal Donation Requests");
            System.out.println("7. Back");

            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    // Register donor first
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Age: ");
                    int age = Integer.parseInt(sc.nextLine());
                    System.out.print("Blood Group: ");
                    String bg = sc.nextLine();
                    System.out.print("City: ");
                    String city = sc.nextLine();
                    System.out.print("Contact: ");
                    String contact = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Last Donated Date (yyyy-mm-dd): ");
                    String lastDonatedDate = sc.nextLine();
                    String donorId = manager.generateDonorId();
                    Donor newDonor = new Donor(donorId, name, age, bg, city, contact, email, lastDonatedDate);
                    manager.addDonor(newDonor);

                    //  FIXED ORDER: Load → Generate ID → Add → Save
                    List<ThalassemiaDonor> currentThalDonors = thal.loadDonors();
                    String thalId = thal.generateThalId(currentThalDonors); 

                    ThalassemiaDonor thalDonor = new ThalassemiaDonor(
                        thalId,
                        donorId,
                        name,
                        age,
                        bg, 
                        city,
                        contact,
                        "N/A", "N/A", "N/A", "N/A", "N/A", "No", "N/A", "N/A"
                    );
                    thal.saveThalDonor(thalDonor);
                    System.out.println(" Thalassemia donor registered with Thal ID: " + thalId);
                    break;

                case "2":
                    while (true) {
                        System.out.println("\n--- Thalassemia Checkup ---");
                        System.out.println("1. Book Thalassemia Checkup");
                        System.out.println("2. Cancel Thalassemia Checkup");
                        System.out.println("3. Back");
                        System.out.print("Choose an option: ");
                        int subChoice = Integer.parseInt(sc.nextLine().trim());

                        if (subChoice == 1) {
                            thal.registerCheckup(sc);
                        } else if (subChoice == 2) {
                            thal.cancelThalCheckup(sc);
                        } else if (subChoice == 3) {
                            break;
                        } else {
                            System.out.println("Invalid option.");
                        }
                    }
                    break;

                case "3":
                    while (true) {
                    System.out.println("\n--- Thalassemia Blood Donation ---");
                    System.out.println("1. Book Donation Appointment");
                    System.out.println("2. Cancel Donation Appointment");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int donationOption = Integer.parseInt(sc.nextLine().trim());

                    if (donationOption == 1) {
                        thal.bookThalDonation(sc);
                    } else if (donationOption == 2) {
                        thal.cancelThalDonation(sc);
                    } else if (donationOption == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }   
                    break;

                case "4":
                    thal.viewThalDashboard();
                    break;
                case "5":
                    System.out.print("Enter your Donor ID: ");
                    String viewId = sc.nextLine().trim();
                    thal.viewThalDonationHistory(viewId);
                    break;
                case "6":
                    thal.viewAndRespondToRequests(sc);
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void showSickleCellMenu(Scanner sc, DonorManager manager, SickleCellManager scdManager, HospitalDirectory hospitalDir) {
    while (true) {
        System.out.println("\n--- Sickle Cell Support ---");
        System.out.println("1. Register as Donor for Sickle Cell");
        System.out.println("2. Book Transfusion Appointment");
        System.out.println("3. View SCD Dashboard");
        System.out.println("4. View My Transfusion History");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                // Step 1: Register Donor
                System.out.print("Name: ");
                String name = sc.nextLine();
                System.out.print("Age: ");
                int age = Integer.parseInt(sc.nextLine());
                System.out.print("Blood Group: ");
                String bg = sc.nextLine();
                System.out.print("City: ");
                String city = sc.nextLine();
                System.out.print("Contact: ");
                String contact = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Last Transfusion Date (yyyy-mm-dd): ");
                String lastDate = sc.nextLine();

                String donorId = manager.generateDonorId();
                Donor donor = new Donor(donorId, name, age, bg, city, contact, email, lastDate);
                manager.addDonor(donor);

                String scdId = scdManager.generateScId();
                System.out.print("Enter SCD Type (SS/SC/Sβ+): ");
                String scdType = sc.nextLine();
                System.out.print("Enter SCD Status (Active/Observation): ");
                String scdStatus = sc.nextLine();

                SickleCellDonor scdDonor = new SickleCellDonor(
                    scdId, donorId, name, age, bg, city, contact, email, scdType, scdStatus, lastDate, "Yes"
                );

                scdManager.appendSickleDonor(scdDonor);
                System.out.println(" Sickle Cell donor registered with SCD ID: " + scdId);
                break;

            case "2":
                while (true) {
                    System.out.println("\n--- Sickle Cell Checkup ---");
                    System.out.println("1. Book Checkup");
                    System.out.println("2. Cancel Checkup");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int sub = Integer.parseInt(sc.nextLine());

                    if (sub == 1) {
                        scdManager.registerSickleCheckup(sc, hospitalDir);
                    } else if (sub == 2) {
                        scdManager.cancelSickleCheckup(sc);
                    } else if (sub == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                break;


            case "3":
                while (true) {
                    System.out.println("\n--- Sickle Cell Blood Donation ---");
                    System.out.println("1. Book Donation Appointment");
                    System.out.println("2. Cancel Donation Appointment");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    int sub = Integer.parseInt(sc.nextLine().trim());

                    if (sub == 1) {
                        scdManager.bookSickleDonation(sc, hospitalDir);
                    } else if (sub == 2) {
                        scdManager.cancelSickleDonation(sc);
                    } else if (sub == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
                break;


            case "4":
                System.out.print("Enter your Donor ID: ");
                String viewDonorId = sc.nextLine().trim();
                scdManager.viewSickleDonationHistory(viewDonorId);
                break;

            case "5":
                return;

            default:
                System.out.println("Invalid choice.");
        }
    }
}

}
