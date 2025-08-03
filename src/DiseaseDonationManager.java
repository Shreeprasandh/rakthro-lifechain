import java.util.List;
import java.util.Scanner;

public class DiseaseDonationManager {

    public static void showDiseaseBasedDonationMenu(Scanner sc, DonorManager manager) {
        ThalassemiaManager thal = new ThalassemiaManager();

        while (true) {
            System.out.println("\n--- Disease-Based Donation System  ---");
            System.out.println("1. Thalassemia Support");
            System.out.println("2. Sickle Cell Anemia (not added yet)");
            System.out.println("3. Hemophilia Assistance (not added yet)");
            System.out.println("4. Back");

            System.out.print("Choose a category: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    showThalassemiaMenu(sc, manager, thal);
                    break;
                case "2":
                    System.out.println("Sickle Cell Anemia support is not implemented yet.");
                    break;
                case "3":
                    System.out.println("Hemophilia Assistance is not implemented yet.");
                    break;
                case "4":
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
            System.out.println("2. Register for Thalassemia Checkup");
            System.out.println("3. Book Thalassemia Blood Donation");
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
                    System.out.print("Last Donated Date (yyyy-mm-dd): ");
                    String lastDonatedDate = sc.nextLine();
                    String donorId = manager.generateDonorId();
                    Donor newDonor = new Donor(donorId, name, age, bg, city, contact, lastDonatedDate);
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
                    thal.registerCheckup(sc);
                    break;
                case "3":
                    thal.bookThalDonation(sc);
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
}
