package InventoryManagement;

import java.text.ParseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {
    private int order_id;
    private Supplier supplier;
    private Status status;
    private String place_date;
    private int place_user;
    private String receive_date;
    private int receive_user;
    private String packed_date;
    private int packed_user;
    private String shipped_date;
    private int shipped_user;
    private String delivered_date;
    private int delivered_user;
    private double price;
    private String edit_date;
    private int edit_user;
    ObservableList<Product> products = FXCollections.observableArrayList();
    
    // constructor
    public Order (int order_id, Supplier supplier, Status status, double price) {
        this.order_id = order_id;
        this.supplier = supplier;
        this.status = status;
        this.price = price;
        this.edit_date = EditDate();
        this.edit_user = EditUser();
    }
    
    public Order (int order_id, Supplier supplier, Status status, String place_date, 
            int place_user, String receive_date, int receive_user, 
            String packed_date, int packed_user, String shipped_date, 
            int shipped_user, String delivered_date, int delivered_user, double price) {
        // ASSIGNING VALUES
        this.order_id = order_id;
        this.supplier = supplier;
        this.status = status;
        this.place_user = place_user;
        this.place_date = place_date;
        this.receive_user = receive_user;
        this.receive_date = receive_date;
        this.packed_user = packed_user;
        this.packed_date = packed_date;
        this.shipped_user = shipped_user;
        this.shipped_date = shipped_date;
        this.delivered_user = delivered_user;
        this.delivered_date = delivered_date;
        this.price = price;
        this.edit_date = EditDate();
        this.edit_user = EditUser();
    }
    
    // calculate last edit order
    public String EditDate() {
        if (status == Status.PLACED) {
            return place_date;
        } else if (status == Status.RECEIVED) {
            return receive_date;
        } else if (status == Status.PACKED) {
            return packed_date;
        } else if (status == Status.SHIPPED) {
            return shipped_date;
        } else if (status == Status.DELIVERED) {
            return delivered_date;
        } else {
            return Common.currentDate();
        }
    }
    
    //EDIT USER FUNCTION
    public int EditUser() {
        if (status == Status.PLACED) {
            return place_user;
        } else if (status == Status.RECEIVED) {
            return receive_user;
        } else if (status == Status.PACKED) {
            return packed_user;
        } else if (status == Status.SHIPPED) {
            return shipped_user;
        } else if (status == Status.DELIVERED) {
            return delivered_user;
        } else {
            return (Common.getUser()).getUserId();
        }
    }
    
    //change status
    public void orderPlaced(int user, String date) {
        this.place_user = user;
        this.place_date = date;
    }
    public void orderReceived(int user, String date) {
        this.receive_user = user;
        this.receive_date = date;
    }
    public void orderPacked(int user, String date) {
        this.packed_user = user;
        this.packed_date = date;
    }
    public void orderShipped(int user, String date) {
        this.shipped_user = user;
        this.shipped_date = date;
    }
    public void orderDelivered(int user, String date) {
        this.delivered_user = user;
        this.delivered_date = date;
    }
    
    // get attributes
    public Status getStatus() {
        return this.status;
    }
    public int getOrderId() {
        return this.order_id;
    }
    public Supplier getSupplier() {
        return this.supplier;
    }
    public ObservableList<Product> getProducts() {
        return this.products;
    }
    public double getPrice () {
        return this.price;
    }
    public String getPriceString() {
        return ("$ " + Common.doubleStr(this.price));
    }
    
    // get dates
    public String getPlaceDate() throws ParseException {
        return Common.displayDate(this.place_date);
    }
    public String getReceivedDate() throws ParseException {
        return Common.displayDate(this.receive_date);
    }
    public String getPackedDate() throws ParseException {
        return Common.displayDate(this.packed_date);
    }
    public String getShippedDate() throws ParseException {
        return Common.displayDate(this.shipped_date);
    }
    public String getDeliveredDate() throws ParseException {
        return Common.displayDate(this.delivered_date);
    }
    public String getEditDate() throws ParseException {
        return Common.displayDate(this.edit_date);
    }
    public String getPlaceDateForm() {
        return this.place_date;
    }
    public String getReceivedDateForm() {
        return this.receive_date;
    }
    public String getPackedDateForm() {
        return this.packed_date;
    }
    public String getShippedDateForm() {
        return this.shipped_date;
    }
    public String getDeliveredDateForm() {
        return this.delivered_date;
    }
    public String getEditDateForm() {
        return this.edit_date;
    }
    
    // get users
    public int getPlaceUser() {
        return this.place_user;
    }
    public int getReceivedUser() {
        return this.receive_user;
    }
    public int getPackedUser() {
        return this.packed_user;
    }
    public int getShippedUser() {
        return this.shipped_user;
    }
    public int getDeliveredUser() {
        return this.delivered_user;
    }
    public int getEditUser() {
        return this.edit_user;
    }
    
    // add product
    public void addProduct(Product product) {
        (this.products).add(product);
    }
}
