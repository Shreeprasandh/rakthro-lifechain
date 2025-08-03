import java.util.Scanner;

public class DiseaseDonationManager {

    public static void showDiseaseBasedDonationMenu(Scanner sc) {
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
                    showThalassemiaMenu(sc); // 👈 You’ll define this next
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

    private static void showThalassemiaMenu(Scanner sc) {
        System.out.println("\n--- Thalassemia Donation Hub  ---");
        System.out.println("1. Book Thalassemia Checkup Appointment");
        System.out.println("2. Donate as Beta Carrier");
        System.out.println("3. Donate as Alpha Carrier");
        System.out.println("4. View Thalassemia Dashboard");
        System.out.println("5. Back");

        System.out.print("Choose an option: ");
        String input = sc.nextLine();

        switch (input) {
            case "1":
                System.out.println(" Booking checkup... (not implemented yet)");
                break;
            case "2":
                System.out.println(" Beta carrier donation... (not implemented yet)");
                break;
            case "3":
                System.out.println(" Alpha carrier donation... (not implemented yet)");
                break;
            case "4":
                System.out.println(" Viewing Thalassemia Dashboard... (not implemented yet)");
                break;
            case "5":
                return;
            default:
                System.out.println("Invalid input.");
        }
    }
}
