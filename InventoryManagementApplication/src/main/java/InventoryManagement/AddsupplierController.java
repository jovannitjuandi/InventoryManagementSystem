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

public class AddsupplierController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Text errorText;
    @FXML private Text loginUserText;
    @FXML private Button addButton;
    @FXML private Button cancelButton;
    //@FXML private Button backButton;s
    @FXML private Button logoutButton;
    @FXML private ComboBox stateInput;
    @FXML private TextField nameInput;
    @FXML private TextField contactInput;
    @FXML private TextField address1Input;
    @FXML private TextField address2Input;
    @FXML private TextField suburbInput;
    @FXML private TextField postcodeInput;
    @FXML private TextField countryInput;
    
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
    
    // INITIALIZE PAGE
    public void initialize() throws SQLException {
        errorText.setVisible(false);
        countryInput.setText("AUSTRALIA");
        countryInput.setDisable(true);
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        
        ObservableList<String> choices = FXCollections.observableArrayList("ACT", "NSW", "QLD", "SA", "TAS", "VIC", "WA");
        stateInput.setItems(choices);
        menuBarDisplay();
    }
    
    // HANDLE FUNCTIONS
    public void handleAdd() throws SQLException , IOException {
        addSupplier();
    }
    
    public void handleCancel() throws IOException {
        switchToSuppliers();
    }
    
    public void handleBack() throws IOException, SQLException {
        switchToSuppliers();
    }
    
    // HELPER FUNCTIONS
    private void addSupplier() throws IOException, SQLException {
        // get and clean input
        String name = Common.clean(nameInput.getText());
        String contact = Common.clean(contactInput.getText());
        String street = Common.clean(address1Input.getText() + " " + address2Input.getText());
        String suburb = Common.clean(suburbInput.getText());
        String state = Common.clean((String) stateInput.getValue());
        String postcode = Common.clean(postcodeInput.getText());
        String country = Common.clean(countryInput.getText());
        
        // check all required inputs are filled (display highest priority first)
        boolean addressComplete = true;
        boolean noError = true;
        if (street.isEmpty() || suburb.isEmpty() || state.isEmpty() || postcode.isEmpty() || country.isEmpty()) {
            addressComplete = false;
        }
        if (!addressComplete || contact.isEmpty() || name.isEmpty() || !Common.isInt(postcode) || postcode.length() != 4) {
            noError = false;
        }
        if (!noError) {
            if (contact.isEmpty()) {
                errorText.setText("Supplier contact cannot be blank");
                errorText.setVisible(true);
            }

            if (!addressComplete) {
                errorText.setText("Supplier Address is incomplete");
                errorText.setVisible(true);
            }

            if (name.isEmpty()) {
                errorText.setText("Supplier name cannot be blank");
                errorText.setVisible(true);
            }
            
            if (!Common.isInt(postcode) || postcode.length() == 4) {
                errorText.setText("Enter a valid four digit postcode");
                errorText.setVisible(true);
            }
        } else {
            // input into the database
            System.out.println(name);
            DatabaseManager.addSupplier(name, contact, street, 
            suburb, state, postcode, country);
            
            // go back to all supplier view
            switchToSuppliers();
        }
    }
    
    private void switchToSuppliers() throws IOException {
        Common.movePage("supplier");
    }
}
