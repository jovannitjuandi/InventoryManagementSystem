package InventoryManagement;

public class Supplier {
    private int id;
    private String name;
    private String contact;
    private Address address;
    
    // CONSTRUCTOR
    public Supplier(int id, String name, String contact, Address address) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }
    
    // GETTER FUNCTIONS
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getContact() {
        return contact;
    }
    public Address getAddress() {
        return address;
    }
    
    // SETTER FUNCTIONS
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    
    //OVERLOADED FUNCTIONS
    @Override
    public String toString () {
        return (this.name);
    }
}
