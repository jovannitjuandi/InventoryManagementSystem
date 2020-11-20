package InventoryManagement;

import java.util.ArrayList;

public class ItemList {
    private static ArrayList<OrderItem> items = new ArrayList<OrderItem>();
    private static ArrayList<OrderItem> remove = new ArrayList<OrderItem>();
    
    public static void addItem(OrderItem entry) {
        boolean found = false;
        for (OrderItem item : items) {
            if (item.productEquals(entry)) {
                item.setQuantity(item.getQuantity() + entry.getQuantity());
                found = true;
            } 
        }
        if (!found) {
            items.add(entry);
        }
    }
    
    private static boolean inDelete(OrderItem input) {
        for (OrderItem item : items) {
            if (item.productEquals(input)) return true;
        }
        return false;
    }
    //DELETE FUNCTIONS
    public static void deleteItem(OrderItem delete) {
        remove.add(delete);
//        boolean zero = false;
//        for (OrderItem item : items) {
//            if (item.productEquals(delete)) {
//                item.setQuantity(item.getQuantity() - delete.getQuantity());
//                if (item.getQuantity() == 0) zero = true;
//            } 
//        }
    }
    
    public static void deleteFromList(OrderItem delete) {
        boolean zero = false;
        for (OrderItem item : items) {
            if (item.productEquals(delete)) {
                item.setQuantity(item.getQuantity() - delete.getQuantity());
                zero = true;
            } 
        }
        if (zero) {
            items.removeIf(entry->entry.getQuantity() == 0);
        }
    }
    
    public static ArrayList<OrderItem> getAddList() {
        return items;
    }
    
    public static ArrayList<OrderItem> getDelList() {
        return remove;
    }
    
    public static ArrayList<OrderItem> gerFinalList() {
        ArrayList<OrderItem> finalList = new ArrayList<OrderItem>();
        for(OrderItem add : items) {
            finalList.add(new OrderItem(add.getProduct(), add.getQuantity(), add.getOrderId()));
        }
        for (OrderItem delete : remove) {
            for (OrderItem entry : finalList) {
                if (entry.productEquals(delete)) {
                    entry.setQuantity(entry.getQuantity() - delete.getQuantity());
                }
            }
        }
        return finalList;
    }
    
    public static void emptyList() {
        items.clear();
        items.removeAll(items);
        remove.clear();
        remove.removeAll(remove);
    }
}
