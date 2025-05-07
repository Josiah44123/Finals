import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileManager {
    private static final String CLIENT_FILE = "CLIENT.txt";
    private static final String RESERVE_FILE = "RESERVE.txt";
    private static final String CHECKED_IN_FILE = "CHECKED-IN.txt";
    private static final String CANCELLED_FILE = "CANCELLED.txt";
    
    // Client file operations
    public void saveClient(Client client) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLIENT_FILE, true))) {
            writer.println(client.toString());
        } catch (IOException e) {
            System.err.println("Error saving client: " + e.getMessage());
        }
    }
    
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Client client = new Client(
                        parts[0], // clientId
                        parts[1], // fullName
                        parts[2], // address
                        parts[3], // contactNumber
                        parts[4]  // email
                    );
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading clients: " + e.getMessage());
        }
        
        return clients;
    }
    
    public Client findClientById(String clientId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CLIENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(clientId)) {
                    return new Client(
                        parts[0], // clientId
                        parts[1], // fullName
                        parts[2], // address
                        parts[3], // contactNumber
                        parts[4]  // email
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error finding client: " + e.getMessage());
        }
        
        return null;
    }
    
    // Reservation file operations
    public void saveReservation(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVE_FILE, true))) {
            writer.println(reservation.toString());
        } catch (IOException e) {
            System.err.println("Error saving reservation: " + e.getMessage());
        }
    }
    
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Reservation reservation = parseReservation(line);
                if (reservation != null) {
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reservations: " + e.getMessage());
        }
        
        return reservations;
    }
    
    public void updateReservation(Reservation updatedReservation) {
        List<Reservation> reservations = getAllReservations();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVE_FILE))) {
            for (Reservation reservation : reservations) {
                if (reservation.getTransactionId().equals(updatedReservation.getTransactionId())) {
                    writer.println(updatedReservation.toString());
                } else {
                    writer.println(reservation.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
    }
    
    // Checked-in file operations
    public void saveCheckedIn(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CHECKED_IN_FILE, true))) {
            writer.println(reservation.toString());
        } catch (IOException e) {
            System.err.println("Error saving checked-in guest: " + e.getMessage());
        }
    }
    
    public List<Reservation> getCheckedInGuests() {
        List<Reservation> checkedInGuests = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CHECKED_IN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Reservation reservation = parseReservation(line);
                if (reservation != null) {
                    checkedInGuests.add(reservation);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading checked-in guests: " + e.getMessage());
        }
        
        return checkedInGuests;
    }
    
    // Cancelled file operations
    public void saveCancelled(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CANCELLED_FILE, true))) {
            writer.println(reservation.toString());
        } catch (IOException e) {
            System.err.println("Error saving cancelled reservation: " + e.getMessage());
        }
    }
    
    public List<Reservation> getCancelledReservations() {
        List<Reservation> cancelledReservations = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CANCELLED_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Reservation reservation = parseReservation(line);
                if (reservation != null) {
                    cancelledReservations.add(reservation);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading cancelled reservations: " + e.getMessage());
        }
        
        return cancelledReservations;
    }
    
    // Helper method to parse reservation from string
    private Reservation parseReservation(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 13) {
                String transactionId = parts[0];
                String clientId = parts[1];
                Date reservationDate = new SimpleDateFormat("MM/dd/yyyy").parse(parts[2]);
                int numGuests = Integer.parseInt(parts[3]);
                String facilityName = parts[4];
                int numFacilities = Integer.parseInt(parts[5]);
                int extraPersons = Integer.parseInt(parts[6]);
                boolean includeLunch = Boolean.parseBoolean(parts[7]);
                boolean includeDinner = Boolean.parseBoolean(parts[8]);
                double totalCost = Double.parseDouble(parts[9]);
                double amountPaid = Double.parseDouble(parts[10]);
                double remainingBalance = Double.parseDouble(parts[11]);
                String status = parts[12];
                
                // Determine facility details based on name
                Facility facility = null;
                switch (facilityName) {
                    case "Single Room":
                        facility = new Facility("Single Room", 1500.00, 2);
                        break;
                    case "Double Room":
                        facility = new Facility("Double Room", 2000.00, 3);
                        break;
                    case "King Room":
                        facility = new Facility("King Room", 3000.00, 4);
                        break;
                    case "Suite":
                        facility = new Facility("Suite", 4000.00, 6);
                        break;
                }
                
                Client client = findClientById(clientId);
                if (client == null) {
                    // Create a placeholder client if not found
                    client = new Client(clientId, "Unknown", "Unknown", "Unknown", "Unknown");
                }
                
                return new Reservation(
                    transactionId, client, reservationDate, numGuests, facility, numFacilities,
                    extraPersons, includeLunch, includeDinner, totalCost, amountPaid,
                    remainingBalance, status
                );
            }
        } catch (ParseException | NumberFormatException e) {
            System.err.println("Error parsing reservation: " + e.getMessage());
        }
        
        return null;
    }
}