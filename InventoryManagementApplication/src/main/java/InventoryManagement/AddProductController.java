package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddProductController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Text errorText;
    @FXML private Text loginUserText;
    @FXML private ComboBox typeInput;
    @FXML private TextField nameInput;
    @FXML private TextField unitInput;
    @FXML private TextField priceInput;
    @FXML private TextField otherInput;
    
    // INITIALIZE PAGE
    public void initialize() throws SQLException {
        errorText.setVisible(false);
        
        ObservableList<String> productTypeChoices = FXCollections.observableArrayList(DatabaseManager.getProductsTypes());
        productTypeChoices.add("other");
        typeInput.setItems(productTypeChoices);
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        menuBarDisplay();
    }
    
    // HANDLE FUNCTIONS
    public void handleAdd() throws IOException, SQLException {
        addProduct();
    }
    
    public void handleBack() throws IOException {
        switchToProducts();
    }
    
    public void handleType() {
        String type = Common.clean((String) typeInput.getValue());
        if (!type.equals("other")) {
            otherInput.setDisable(true);
        } else {
            otherInput.setDisable(false);
        }
    }
    
    // MENU BAR HANDLES
    @FXML private Button userButton;
    @FXML private Button supplierButton;
    public void handleLogout() throws IOException {
        Common.movePage("login");
    }
    
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
    
    // HELPER FUNCTIONS
    private void switchToProducts() throws IOException {
        Common.movePage("product");
    }
    
    private void addProduct() throws SQLException, IOException {
        String name = Common.clean(nameInput.getText());
        String unit = Common.clean(unitInput.getText());
        String price = Common.clean(priceInput.getText());
        String type = Common.clean((String) typeInput.getValue());
        String other = Common.clean(otherInput.getText());

        if (name.isEmpty() || unit.isEmpty() || price.isEmpty() || type.isEmpty()) {
            errorText.setText("Please fill in all required fields!");
            errorText.setVisible(true);
        } else if (type.toLowerCase().equals("other") && other.isEmpty()) {
            errorText.setText("Please specify the product category!");
            errorText.setVisible(true); 
        } else if (!Common.isNumber(price)) {
            errorText.setText("Please input a valid price");
            errorText.setVisible(true); 
        } else if (unit.matches(".*\\d.*")) {
            errorText.setText("'Unit' cannot contain any numbers!");
            errorText.setVisible(true);        
        } else {
            if (type.toLowerCase().equals("other")) {
                DatabaseManager.addProductType(other);
                int prodTypeId = DatabaseManager.getProductTypeId(other);
                DatabaseManager.addProduct(name, unit, Double.parseDouble(price), prodTypeId);
            } else {
                int prodTypeId = DatabaseManager.getProductTypeId(type);
                DatabaseManager.addProduct(name, unit, Double.parseDouble(price), prodTypeId);                
            }
            switchToProducts();
        }
    }
}