package InventoryManagement;

public class Address {
    private String street;
    private String suburb;
    private String state;
    private String postcode;
    private String country;
    
    // CONSTRUCTOR
    public Address (String str, String sub, String sta, String poc, String cou){
        street = str;
        suburb = sub;
        state = sta;
        postcode = poc;
        country = cou;
    }
    
    // GETTER FUNCTIONS
    public String getStreet() {
        return street;
    }
    public String getSuburb() {
        return suburb;
    }
    public String getState() {
        return state;
    }
    public String getPostcode() {
        return postcode;
    }
    public String getCountry() {
        return country;
    }
    
    // SETTER FUNCTIONS
    public void setStreet(String str) {
        street = str;
    }
    public void setSuburb(String sub) {
        suburb = sub;
    }
    public void setState(String sta) {
        state = sta;
    }
    public void setPostcode(String poc) {
        postcode = poc;
    }
    public void setCountry(String cou) {
        country = cou;
    }
    
    // OVERLOADED FUNCTIONS
    @Override public String toString() { 
        return street + " " + suburb.toUpperCase() + " " + state.toUpperCase() 
            + " " + postcode + " " + country.toUpperCase(); 
    }
}
