import java.io.Serializable;

public class Facility implements Serializable {
    private String name;
    private double price;
    private int maxPax;
    
    public Facility(String name, double price, int maxPax) {
        this.name = name;
        this.price = price;
        this.maxPax = maxPax;
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getMaxPax() {
        return maxPax;
    }
    
    public void setMaxPax(int maxPax) {
        this.maxPax = maxPax;
    }
    
    @Override
    public String toString() {
        return name + "," + price + "," + maxPax;
    }
}