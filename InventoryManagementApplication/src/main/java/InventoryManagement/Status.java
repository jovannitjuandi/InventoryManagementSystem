package InventoryManagement;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    PLACED(0),
    RECEIVED(1),
    PACKED(2), 
    SHIPPED(3),
    DELIVERED(4);
    
    private int statNo;
    private static Map<Integer, Status> map = new HashMap<Integer, Status>();
    
    static {
        for (Status stats : Status.values()) {
            map.put(stats.statNo, stats);
        }
    }
    
    public static Status valueOf(int statNo) {
        return (Status) map.get(statNo);
    }
    
    private Status(final int num) { 
        statNo = num; 
    }
    
    public int getValue() {
        return statNo;
    }
}
