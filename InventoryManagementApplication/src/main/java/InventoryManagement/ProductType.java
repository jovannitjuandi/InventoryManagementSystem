package InventoryManagement;

public class ProductType {
    private int id;
    private String type;
    
    //CONSTRUCTOR
    public ProductType(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    //GETTER FUNCTIONS
    public int getId() {
        return this.id;
    }
    public String getType() {
        return this.type;
    }
}
