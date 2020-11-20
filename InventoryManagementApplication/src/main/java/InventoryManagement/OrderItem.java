package InventoryManagement;
import java.sql.SQLException;

public class OrderItem {
    private Product item;
    private int quantity;
    private int order_id;
    private double total;
    
    //CONSTRUCTOR
    public OrderItem(Product item, int qty, int order_id) {
        this.item = item;
        this.quantity = qty;
        this.order_id = order_id;
        this.total = (this.quantity*item.getPrice());
    }
    
    //GETTER FUNCTIONS
    public Product getProduct() {
        return item;
    }
    public int getProductId() {
        return item.getProdId();
    }
    public String getProductName() {
        return item.getName();
    }
    public int getTypeId() {
        return item.getTypeId();
    }
    public int getOrderId() {
        return this.order_id;
    }
    public String getUnit() {
        return item.getUnit();
    }
    public double getTotal() {
        return this.quantity*item.getPrice();
    }
    public String getTotalString() {
        return Common.doubleStr(this.quantity*item.getPrice());
    }
    public int getQuantity() {
        return quantity;
    }
    public String getPriceString() {
        return this.item.getPriceString();
    }
    
    //SETTER FUNCTIONS
    public void setQuantity (int qty) {
        this.quantity = qty;
        this.total = (this.quantity*item.getPrice());
    }
    
    public boolean productEquals(OrderItem o) {
        if (this.item.equals(o.getProduct())) {
            return true;
        }
        return false;
    }
    
    //OVEROADED FUNCTIONS
    @Override
    public boolean equals(Object o) { 
        OrderItem cmp = (OrderItem) o;
        if((this.item.getProdId() == cmp.getProductId()) && 
          (this.quantity == cmp.getQuantity()) && (this.order_id == cmp.getOrderId())) return true;
        return false;
    }
}
