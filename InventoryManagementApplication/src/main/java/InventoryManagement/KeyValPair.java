package InventoryManagement;

public class KeyValPair {
    private int key;
    private String value;
    
    //CONSTRUCTOR 
    public KeyValPair(int k, String val) {
        key = k;
        value = val;
    }
    
    //GETTER FUNCTIONS
    public int getKey() {
        return key;
    }
    public void setKey(int k) {
        key = k;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String val) {
        value = val;
    }
    
    //OVERLOADED FUNCTIONS
    @Override public String toString() { 
        return value; 
    }
}
