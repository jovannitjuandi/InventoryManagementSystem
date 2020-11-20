package InventoryManagement;


public class User {
    private int user_id;
    private String name;
    private String username;
    private String password;
    private Position status;
    private int supp_id;
    private boolean approved;
    
    //CONSTRUCTOR
    public User(int ui, String n, String u, String p, Position s, boolean a, int si) {
        user_id = ui;
        name = n;
        username = u;
        password = p;
        status = s;
        approved = a;
        supp_id = si;
    }
    
        public User(int ui, String n, String u, String p, Position s, boolean a) {
        user_id = ui;
        name = n;
        username = u;
        password = p;
        status = s;
        approved = a;
        } 
    
    
    public User() {
        user_id = -1;
        name = "";
        username = "";
        password = "";
        supp_id = -1;
    }
    
    // GETTER FUNCTIONS
    public int getUserId() {
        return user_id;
    }
    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public Position getStatus() {
        return status;
    }
    public int getSuppId() {
        return supp_id;
    }
    
    public boolean getApprovedStatus() {
        return approved;
    }
    
    public String getApprovedString() {
        if (approved) {
            return "APPROVED";
        } else {
            return "NOT APPROVED";
        }
    }
    
    // SETTER FUNCTIONS
    public void setUserId(int id) {
        user_id = id;
    }
    public void setName(String n) {
        name = n;
    }
    public void setUsername(String u) {
        username = u;
    }
    public void setStatus(Position s) {
        status = s;
    }
    public void setSuppId(int id) {
        supp_id = id;
    }
    
    public void setApprovedStatus(boolean approval) {
        approved = approval;
    }
    
    //OVERLOAD FUNCTIONS
    @Override
    public String toString() {
        return "User{" + "user_id=" + user_id + ", name=" + name + ", username=" + username + ", password=" + password + ", status=" + status + ", supp_id=" + supp_id + '}';
    }
    }

