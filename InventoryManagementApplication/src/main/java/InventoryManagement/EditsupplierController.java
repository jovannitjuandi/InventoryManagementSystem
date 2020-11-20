package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditsupplierController {
    @FXML private TextField nameInput;
    @FXML private TextField contactInput;
    @FXML private TextField streetInput;
    @FXML private TextField suburbInput;
    @FXML private TextField postcodeInput;
    @FXML private TextField countryInput;
    @FXML private Text errorText;
    @FXML private Text confirmessage;
    @FXML private Text loginUserText;
    @FXML private ComboBox stateInput;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    @FXML private Button logoutButton;
    @FXML private Button predelete;
    @FXML private Button dontdelete;
    
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
    
    //HANDLE FUNCTIONS
    @FXML
    private void handleBack() throws IOException {
        switchToSuppliers();    
    }
    
    //HELPER FUNCTIONS
    private void switchToSuppliers() throws IOException {
        Common.movePage("supplier");
    }
    
    public void previousValue() throws SQLException {
        // get the id from common page
        int id = Common.getIdFromPage();
        
        // get the supplier by id
        Supplier current = DatabaseManager.getSupplierById(id);
        Address address = current.getAddress();
        
        // put the values in the fields
        //supplierID.setText(Integer.toString(id));
        nameInput.setText(current.getName());
        contactInput.setText(current.getContact());
        streetInput.setText(address.getStreet());
        suburbInput.setText(address.getSuburb());
        stateInput.setValue(address.getState().toUpperCase());
        postcodeInput.setText(address.getPostcode());
        countryInput.setText(address.getCountry());
    }   
    
    public void confirmDelete() throws IOException{
        confirmessage.setVisible(true);
        dontdelete.setVisible(true);
        deleteButton.setVisible(true);
        addButton.setVisible(false);
        predelete.setVisible(false);
        cancelButton.setVisible(false);

    }

    public void cancelDelete() throws IOException{
        confirmessage.setVisible(false);
        dontdelete.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(true);
        predelete.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    public void handleSave() throws SQLException, IOException {
        // get the id from common page
        int id = Common.getIdFromPage();
        
        // get the new values from the fields
        String name = Common.clean(nameInput.getText());
        String contact = Common.clean(contactInput.getText());
        String street = Common.clean(streetInput.getText());
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
        if (!addressComplete || contact.isEmpty() || name.isEmpty() || postcode.length() != 4 || !Common.isInt(postcode)) {
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
            
            if (postcode.length() != 4 || !Common.isInt(postcode)) {
                errorText.setText("please enter a valid 4 digit australian postcode");
                errorText.setVisible(true);
            }
        } else {
            // update the statements
            DatabaseManager.editSupplier(id, name, contact, 
            street, suburb, state, postcode, country);
            // move to another page
            switchToSuppliers();
        }
    }
    
    public void handleCancel() throws IOException {
        switchToSuppliers();
    }
    
    public void handleDelete() throws SQLException, IOException {
        DatabaseManager.removeSupplier(Common.getIdFromPage());
        switchToSuppliers();
    }
   
    //INITIALIZE PAGE 
    public void initialize() throws SQLException {
        ObservableList<String> choices = FXCollections.observableArrayList("ACT", "NSW", "QLD", "SA", "TAS", "VIC", "WA");
        stateInput.setItems(choices);
        errorText.setVisible(false);
        previousValue();
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        menuBarDisplay();
        deleteButton.setVisible(false);
    }
}
