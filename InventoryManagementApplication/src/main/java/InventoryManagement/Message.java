package InventoryManagement;

public class Message {
    private static Object message;
    private static String target;
     
    public static boolean messageExist() {
        if (message == null) {
            return false;
        } else if (!(target.equals(Common.getPage()))) {
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean messageFree() {
        if (message == null && target == null) {
            return true;
        } else {
            return false;
        }
    }
    
    public static Object receiveMessage() {
        Object msg = null;
        String currentPage = Common.getPage();
        
        if (currentPage.equals(target)) {
            msg = message;
        }
        deleteMessage();
        return msg;
    }
    
    public static void sendMessage(String t) {
        target = t;
    }
    
    public static boolean createMessage(Object msg, String t) {
        if (messageFree()) {
            target = t;
            message = msg;
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean createMessage(Object msg) {
        if (messageFree()) {
            message = msg;
            return true;
        } else {
            return false;
        }
    }
    
    public static void deleteMessage() {
        message = null;
        target = null;
    }
}
