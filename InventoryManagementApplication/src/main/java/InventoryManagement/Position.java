package InventoryManagement;

import java.util.HashMap;
import java.util.Map;

public enum Position {
    MANAGER(1),
    EMPLOYEE(2),
    SUPPLIER(3);
    
    private int posNo;
    private static Map<Integer, Position> map = new HashMap<Integer, Position>();
    
    static {
        for (Position pos : Position.values()) {
            map.put(pos.posNo, pos);
        }
    }
    
    public static Position valueOf(int posNo) {
        return (Position) map.get(posNo);
    }
    
    private Position(final int num) { 
        posNo = num; 
    }
    
    //GETTER FUNCTION
    public int getValue() {
        return posNo;
    }
}
