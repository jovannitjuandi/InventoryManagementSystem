package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class AboutController {
    public void handleBack() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    public void handleLogout() throws IOException {
        Common.movePage("login");
    }
    
    public void initialize() throws SQLException {
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        menuBarDisplay();
    }
    
    // MENU BAR HANDLES
    @FXML private Button userButton;
    @FXML private Button supplierButton;
    @FXML private Text loginUserText;
    
    public void handleHome() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    public void handleSupplier() throws IOException {
        Common.movePage("Supplier");
    }
    
    public void handleAbout() throws IOException {
        Common.movePage("about");
    }
    
    public void handleProduct() throws IOException {
        Common.movePage("product");
    }
    
    public void handleOrder() throws IOException {
        Common.movePage("order");
    }
    
    public void handleUser() throws IOException {
        Common.movePage("user");
    }
    
    private void menuBarDisplay() throws SQLException {
        User currentUser = Common.getUser();
        if (currentUser.getStatus() == Position.MANAGER) {
        } else if (currentUser.getStatus() == Position.EMPLOYEE) {
            userButton.setVisible(false);
        } else if (currentUser.getStatus() == Position.SUPPLIER) {
            userButton.setVisible(false);
            supplierButton.setVisible(false);
        }
    }
}