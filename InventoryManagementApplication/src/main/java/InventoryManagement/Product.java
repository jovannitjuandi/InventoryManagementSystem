package InventoryManagement;
import java.text.DecimalFormat;

public class Product {
    private int prod_id;
    private String name;
    private String unit;
    private double price;
    private boolean selected;
    private ProductType type;
    private boolean available;
    
    //CONSTRUCTOR
    public Product (int prod_id, String name, String unit, double price, ProductType type, boolean available, boolean selected) {
        this.prod_id = prod_id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.type = type;
        this.selected = false;
    }
    
    //GETTER FUNCTIONS
    public int getProdId() {
        return prod_id;
    }
    public String getName() {
        return name;
    }
    public String getUnit() {
        return unit;
    }
    public boolean getAvailability() {
        return available;
    }
    public double getPrice() {
        return price;
    }
    public boolean getSelected() {
        return this.selected;
    }
    public String getTypeName() {
        return this.type.getType();
    }
    public int getTypeId() {
        return this.type.getId();
    }
    
    //SETTER FUNCTIONS
    public void setSelected(boolean selected){
        this.selected = selected;
    }
   
    private static DecimalFormat df = new DecimalFormat("#.#");
    public String getPriceString() {
        return (df.format(price));
    }
    
    //OVERLOADED FUNCTIONS
    @Override
    public boolean equals(Object o) { 
        if (this == o) return true;
        Product cmp = (Product) o;
        if(this.prod_id == cmp.getProdId()) return true;
        return false;
    }
}
