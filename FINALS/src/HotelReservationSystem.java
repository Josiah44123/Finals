import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager();
        AuthenticationManager authManager = new AuthenticationManager(fileManager);
        ReservationManager reservationManager = new ReservationManager(fileManager);
        
        System.out.println("Welcome to DLSL Hotel Reservation System");
        
        boolean exit = false;
        while (!exit) {
            System.out.println("\nSelect user type:");
            System.out.println("1. Client");
            System.out.println("2. Receptionist");
            System.out.println("3. Manager");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    clientMenu(scanner, authManager, reservationManager, fileManager);
                    break;
                case 2:
                    receptionistMenu(scanner, authManager, reservationManager, fileManager);
                    break;
                case 3:
                    managerMenu(scanner, authManager, reservationManager, fileManager);
                    break;
                case 4:
                    exit = true;
                    System.out.println("Thank you for using DLSL Hotel Reservation System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void clientMenu(Scanner scanner, AuthenticationManager authManager, 
                                  ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Client Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Make Reservation");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1 -> registerClient(scanner, fileManager);
            case 2 -> makeReservation(scanner, authManager, reservationManager, fileManager);
            case 3 -> {
                return;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void registerClient(Scanner scanner, FileManager fileManager) {
        System.out.println("\n--- Client Registration ---");
        
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        
        // Generate client ID (simple implementation)
        String clientId = "CL" + System.currentTimeMillis() % 10000;
        
        Client client = new Client(clientId, fullName, address, contactNumber, email);
        fileManager.saveClient(client);
        
        System.out.println("\nRegistration successful!");
        System.out.println("Your Client ID is: " + clientId);
        System.out.println("Please keep this ID for future reservations.");
    }
    
    private static void makeReservation(Scanner scanner, AuthenticationManager authManager, 
                                       ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Make Reservation ---");
        
        System.out.print("Enter your Client ID: ");
        String clientId = scanner.nextLine();
        
        Client client = authManager.validateClient(clientId);
        if (client == null) {
            System.out.println("Client not found. Please register first.");
            return;
        }
        
        System.out.println("Welcome, " + client.getFullName() + "!");
        
        // Get reservation details
        System.out.print("Enter reservation date (MM/dd/yyyy): ");
        String dateStr = scanner.nextLine();
        Date reservationDate = null;
        try {
            reservationDate = new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use MM/dd/yyyy.");
            return;
        }
        
        System.out.print("Enter number of guests: ");
        int numGuests = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        // Display available facilities
        System.out.println("\nAvailable Facilities:");
        System.out.println("1. Single Room - P1,500.00 (max 2 pax)");
        System.out.println("2. Double Room - P2,000.00 (max 3 pax)");
        System.out.println("3. King Room - P3,000.00 (max 4 pax)");
        System.out.println("4. Suite - P4,000.00 (max 6 pax)");
        
        System.out.print("Select facility (1-4): ");
        int facilityChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Facility facility = null;
        switch (facilityChoice) {
            case 1:
                facility = new Facility("Single Room", 1500.00, 2);
                break;
            case 2:
                facility = new Facility("Double Room", 2000.00, 3);
                break;
            case 3:
                facility = new Facility("King Room", 3000.00, 4);
                break;
            case 4:
                facility = new Facility("Suite", 4000.00, 6);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        System.out.print("Enter number of facilities to reserve: ");
        int numFacilities = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        // Calculate extra person fee
        int maxPax = facility.getMaxPax() * numFacilities;
        int extraPersons = Math.max(0, numGuests - maxPax);
        double extraPersonFee = extraPersons * 500.00;
        
        // Meal options
        System.out.println("\nMeal Options:");
        System.out.println("Breakfast is included for free.");
        System.out.print("Include lunch (P250.00 per person)? (y/n): ");
        boolean includeLunch = scanner.nextLine().toLowerCase().startsWith("y");
        
        System.out.print("Include dinner (P350.00 per person)? (y/n): ");
        boolean includeDinner = scanner.nextLine().toLowerCase().startsWith("y");
        
        double lunchCost = includeLunch ? numGuests * 250.00 : 0;
        double dinnerCost = includeDinner ? numGuests * 350.00 : 0;
        
        // Calculate total cost
        double facilityCost = facility.getPrice() * numFacilities;
        double totalCost = facilityCost + extraPersonFee + lunchCost + dinnerCost;
        
        // Payment options
        System.out.println("\nTotal cost: P" + String.format("%.2f", totalCost));
        System.out.println("Payment Options:");
        System.out.println("1. Pay 30% reservation fee (P" + String.format("%.2f", totalCost * 0.3) + ")");
        System.out.println("2. Pay full amount (P" + String.format("%.2f", totalCost) + ")");
        System.out.print("Select payment option (1-2): ");
        int paymentOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        double amountPaid = (paymentOption == 1) ? totalCost * 0.3 : totalCost;
        double remainingBalance = totalCost - amountPaid;
        
        // Generate transaction ID
        String transactionId = "TX" + System.currentTimeMillis() % 10000;
        
        // Create reservation
        Reservation reservation = new Reservation(
            transactionId,
            client,
            reservationDate,
            numGuests,
            facility,
            numFacilities,
            extraPersons,
            includeLunch,
            includeDinner,
            totalCost,
            amountPaid,
            remainingBalance,
            "Reserved"
        );
        
        reservationManager.saveReservation(reservation);
        
        System.out.println("\nReservation successful!");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Amount Paid: P" + String.format("%.2f", amountPaid));
        if (remainingBalance > 0) {
            System.out.println("Remaining Balance: P" + String.format("%.2f", remainingBalance));
        }
    }
    
    private static void receptionistMenu(Scanner scanner, AuthenticationManager authManager, 
                                        ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Receptionist Menu ---");
        System.out.println("1. View Client Information");
        System.out.println("2. View Reservations");
        System.out.println("3. Process Check-in");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                viewClientInformation(scanner, fileManager);
                break;
            case 2:
                viewReservations(scanner, reservationManager);
                break;
            case 3:
                processCheckIn(scanner, reservationManager, fileManager);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void viewClientInformation(Scanner scanner, FileManager fileManager) {
        System.out.println("\n--- Client Information ---");
        List<Client> clients = fileManager.getAllClients();
        
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }
        
        for (Client client : clients) {
            System.out.println("\nClient ID: " + client.getClientId());
            System.out.println("Name: " + client.getFullName());
            System.out.println("Address: " + client.getAddress());
            System.out.println("Contact: " + client.getContactNumber());
            System.out.println("Email: " + client.getEmail());
            System.out.println("------------------------");
        }
    }
    
    private static void viewReservations(Scanner scanner, ReservationManager reservationManager) {
        System.out.println("\n--- View Reservations ---");
        System.out.println("1. View All Reservations");
        System.out.println("2. Sort by Date");
        System.out.println("3. Filter by Remaining Balance");
        System.out.println("4. Filter by Paid in Full");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        List<Reservation> reservations = null;
        
        switch (choice) {
            case 1:
                reservations = reservationManager.getAllReservations();
                break;
            case 2:
                reservations = reservationManager.getReservationsSortedByDate();
                break;
            case 3:
                reservations = reservationManager.getReservationsWithBalance();
                break;
            case 4:
                reservations = reservationManager.getReservationsPaidInFull();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        
        displayReservations(reservations);
    }
    
    private static void displayReservations(List<Reservation> reservations) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        for (Reservation res : reservations) {
            System.out.println("\nTransaction ID: " + res.getTransactionId());
            System.out.println("Client: " + res.getClient().getFullName() + " (ID: " + res.getClient().getClientId() + ")");
            System.out.println("Date: " + dateFormat.format(res.getReservationDate()));
            System.out.println("Facility: " + res.getFacility().getName() + " x" + res.getNumFacilities());
            System.out.println("Guests: " + res.getNumGuests() + " (Extra: " + res.getExtraPersons() + ")");
            System.out.println("Meals: Breakfast (Free)" + 
                              (res.isIncludeLunch() ? ", Lunch" : "") + 
                              (res.isIncludeDinner() ? ", Dinner" : ""));
            System.out.println("Total Cost: P" + String.format("%.2f", res.getTotalCost()));
            System.out.println("Amount Paid: P" + String.format("%.2f", res.getAmountPaid()));
            System.out.println("Remaining: P" + String.format("%.2f", res.getRemainingBalance()));
            System.out.println("Status: " + res.getStatus());
            System.out.println("------------------------");
        }
    }
    
    private static void processCheckIn(Scanner scanner, ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Process Check-in ---");
        System.out.println("1. Search by Transaction ID");
        System.out.println("2. Search by Client Name");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Reservation reservation = null;
        
        switch (choice) {
            case 1:
                System.out.print("Enter Transaction ID: ");
                String transactionId = scanner.nextLine();
                reservation = reservationManager.findReservationByTransactionId(transactionId);
                break;
            case 2:
                System.out.print("Enter Client Name: ");
                String clientName = scanner.nextLine();
                List<Reservation> clientReservations = reservationManager.findReservationsByClientName(clientName);
                
                if (clientReservations.isEmpty()) {
                    System.out.println("No reservations found for this client.");
                    return;
                }
                
                if (clientReservations.size() > 1) {
                    System.out.println("Multiple reservations found. Please select one:");
                    for (int i = 0; i < clientReservations.size(); i++) {
                        Reservation res = clientReservations.get(i);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        System.out.println((i+1) + ". " + res.getTransactionId() + " - " + 
                                          dateFormat.format(res.getReservationDate()) + " - " + 
                                          res.getFacility().getName());
                    }
                    
                    System.out.print("Select reservation (1-" + clientReservations.size() + "): ");
                    int resChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    if (resChoice < 1 || resChoice > clientReservations.size()) {
                        System.out.println("Invalid choice.");
                        return;
                    }
                    
                    reservation = clientReservations.get(resChoice - 1);
                } else {
                    reservation = clientReservations.get(0);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }
        
        // Display reservation details
        System.out.println("\nReservation Details:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("Transaction ID: " + reservation.getTransactionId());
        System.out.println("Client: " + reservation.getClient().getFullName());
        System.out.println("Date: " + dateFormat.format(reservation.getReservationDate()));
        System.out.println("Facility: " + reservation.getFacility().getName() + " x" + reservation.getNumFacilities());
        System.out.println("Status: " + reservation.getStatus());
        
        if (reservation.getStatus().equals("Guest Checked-In")) {
            System.out.println("This guest has already checked in.");
            return;
        }
        
        if (reservation.getRemainingBalance() > 0) {
            System.out.println("Remaining Balance: P" + String.format("%.2f", reservation.getRemainingBalance()));
            System.out.print("Collect remaining balance? (y/n): ");
            boolean collectBalance = scanner.nextLine().toLowerCase().startsWith("y");
            
            if (collectBalance) {
                reservation.setAmountPaid(reservation.getTotalCost());
                reservation.setRemainingBalance(0);
                System.out.println("Payment collected successfully.");
            }
        }
        
        // Update status to checked-in
        reservation.setStatus("Guest Checked-In");
        reservationManager.updateReservation(reservation);
        fileManager.saveCheckedIn(reservation);
        
        System.out.println("Check-in processed successfully!");
    }
    
    private static void managerMenu(Scanner scanner, AuthenticationManager authManager, 
                                   ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Manager Menu ---");
        System.out.println("1. View All Records");
        System.out.println("2. View Checked-in Guests");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View Cancelled Reservations");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                viewAllRecords(reservationManager);
                break;
            case 2:
                viewCheckedInGuests(fileManager);
                break;
            case 3:
                cancelReservation(scanner, reservationManager, fileManager);
                break;
            case 4:
                viewCancelledReservations(fileManager);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private static void viewAllRecords(ReservationManager reservationManager) {
        System.out.println("\n--- All Reservation Records ---");
        List<Reservation> reservations = reservationManager.getAllReservations();
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        
        displayReservations(reservations);
    }
    
    private static void viewCheckedInGuests(FileManager fileManager) {
        System.out.println("\n--- Checked-in Guests ---");
        List<Reservation> checkedInGuests = fileManager.getCheckedInGuests();
        
        if (checkedInGuests.isEmpty()) {
            System.out.println("No checked-in guests found.");
            return;
        }
        
        displayReservations(checkedInGuests);
    }
    
    private static void cancelReservation(Scanner scanner, ReservationManager reservationManager, FileManager fileManager) {
        System.out.println("\n--- Cancel Reservation ---");
        System.out.print("Enter Transaction ID to cancel: ");
        String transactionId = scanner.nextLine();
        
        Reservation reservation = reservationManager.findReservationByTransactionId(transactionId);
        
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }
        
        if (reservation.getStatus().equals("Guest Checked-In")) {
            System.out.println("Cannot cancel a reservation that has already been checked in.");
            return;
        }
        
        if (reservation.getStatus().equals("Cancelled")) {
            System.out.println("This reservation has already been cancelled.");
            return;
        }
        
        // Display reservation details
        System.out.println("\nReservation Details:");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println("Transaction ID: " + reservation.getTransactionId());
        System.out.println("Client: " + reservation.getClient().getFullName());
        System.out.println("Date: " + dateFormat.format(reservation.getReservationDate()));
        System.out.println("Facility: " + reservation.getFacility().getName() + " x" + reservation.getNumFacilities());
        
        System.out.print("Are you sure you want to cancel this reservation? (y/n): ");
        boolean confirmCancel = scanner.nextLine().toLowerCase().startsWith("y");
        
        if (!confirmCancel) {
            System.out.println("Cancellation aborted.");
            return;
        }
        
        // Update status to cancelled
        reservation.setStatus("Cancelled");
        reservationManager.updateReservation(reservation);
        fileManager.saveCancelled(reservation);
        
        System.out.println("Reservation cancelled successfully!");
    }
    
    private static void viewCancelledReservations(FileManager fileManager) {
        System.out.println("\n--- Cancelled Reservations ---");
        List<Reservation> cancelledReservations = fileManager.getCancelledReservations();
        
        if (cancelledReservations.isEmpty()) {
            System.out.println("No cancelled reservations found.");
            return;
        }
        
        displayReservations(cancelledReservations);
    }
}