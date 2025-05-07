import java.util.*;

public class ReservationManager {
    private FileManager fileManager;
    
    public ReservationManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    public void saveReservation(Reservation reservation) {
        fileManager.saveReservation(reservation);
    }
    
    public void updateReservation(Reservation reservation) {
        fileManager.updateReservation(reservation);
    }
    
    public List<Reservation> getAllReservations() {
        return fileManager.getAllReservations();
    }
    
    public List<Reservation> getReservationsSortedByDate() {
        List<Reservation> reservations = fileManager.getAllReservations();
        Collections.sort(reservations, Comparator.comparing(Reservation::getReservationDate));
        return reservations;
    }
    
    public List<Reservation> getReservationsWithBalance() {
        List<Reservation> reservations = fileManager.getAllReservations();
        List<Reservation> withBalance = new ArrayList<>();
        
        for (Reservation reservation : reservations) {
            if (reservation.getRemainingBalance() > 0 && !reservation.getStatus().equals("Cancelled")) {
                withBalance.add(reservation);
            }
        }
        
        return withBalance;
    }
    
    public List<Reservation> getReservationsPaidInFull() {
        List<Reservation> reservations = fileManager.getAllReservations();
        List<Reservation> paidInFull = new ArrayList<>();
        
        for (Reservation reservation : reservations) {
            if (reservation.getRemainingBalance() == 0 && !reservation.getStatus().equals("Cancelled")) {
                paidInFull.add(reservation);
            }
        }
        
        return paidInFull;
    }
    
    public Reservation findReservationByTransactionId(String transactionId) {
        List<Reservation> reservations = fileManager.getAllReservations();
        
        for (Reservation reservation : reservations) {
            if (reservation.getTransactionId().equals(transactionId)) {
                return reservation;
            }
        }
        
        return null;
    }
    
    public List<Reservation> findReservationsByClientName(String clientName) {
        List<Reservation> reservations = fileManager.getAllReservations();
        List<Reservation> clientReservations = new ArrayList<>();
        
        for (Reservation reservation : reservations) {
            if (reservation.getClient().getFullName().toLowerCase().contains(clientName.toLowerCase())) {
                clientReservations.add(reservation);
            }
        }
        
        return clientReservations;
    }
}
