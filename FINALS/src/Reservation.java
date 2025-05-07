import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation implements Serializable {
    private String transactionId;
    private Client client;
    private Date reservationDate;
    private int numGuests;
    private Facility facility;
    private int numFacilities;
    private int extraPersons;
    private boolean includeLunch;
    private boolean includeDinner;
    private double totalCost;
    private double amountPaid;
    private double remainingBalance;
    private String status; // "Reserved", "Guest Checked-In", "Cancelled"
    
    public Reservation(String transactionId, Client client, Date reservationDate, int numGuests, 
                      Facility facility, int numFacilities, int extraPersons, boolean includeLunch, 
                      boolean includeDinner, double totalCost, double amountPaid, 
                      double remainingBalance, String status) {
        this.transactionId = transactionId;
        this.client = client;
        this.reservationDate = reservationDate;
        this.numGuests = numGuests;
        this.facility = facility;
        this.numFacilities = numFacilities;
        this.extraPersons = extraPersons;
        this.includeLunch = includeLunch;
        this.includeDinner = includeDinner;
        this.totalCost = totalCost;
        this.amountPaid = amountPaid;
        this.remainingBalance = remainingBalance;
        this.status = status;
    }
    
    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Date getReservationDate() {
        return reservationDate;
    }
    
    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
    
    public int getNumGuests() {
        return numGuests;
    }
    
    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }
    
    public Facility getFacility() {
        return facility;
    }
    
    public void setFacility(Facility facility) {
        this.facility = facility;
    }
    
    public int getNumFacilities() {
        return numFacilities;
    }
    
    public void setNumFacilities(int numFacilities) {
        this.numFacilities = numFacilities;
    }
    
    public int getExtraPersons() {
        return extraPersons;
    }
    
    public void setExtraPersons(int extraPersons) {
        this.extraPersons = extraPersons;
    }
    
    public boolean isIncludeLunch() {
        return includeLunch;
    }
    
    public void setIncludeLunch(boolean includeLunch) {
        this.includeLunch = includeLunch;
    }
    
    public boolean isIncludeDinner() {
        return includeDinner;
    }
    
    public void setIncludeDinner(boolean includeDinner) {
        this.includeDinner = includeDinner;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public double getRemainingBalance() {
        return remainingBalance;
    }
    
    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return transactionId + "," + client.getClientId() + "," + dateFormat.format(reservationDate) + "," +
               numGuests + "," + facility.getName() + "," + numFacilities + "," + extraPersons + "," +
               includeLunch + "," + includeDinner + "," + totalCost + "," + amountPaid + "," +
               remainingBalance + "," + status;
    }
}