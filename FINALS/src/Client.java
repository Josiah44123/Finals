import java.io.Serializable;

public class Client implements Serializable {
    private String clientId;
    private String fullName;
    private String address;
    private String contactNumber;
    private String email;
    
    public Client(String clientId, String fullName, String address, String contactNumber, String email) {
        this.clientId = clientId;
        this.fullName = fullName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }
    
    // Getters and setters
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return clientId + "," + fullName + "," + address + "," + contactNumber + "," + email;
    }
}