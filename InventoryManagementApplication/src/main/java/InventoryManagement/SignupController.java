package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class SignupController {
    // DECLARE DESIGN COMPONENTS
    @FXML private RadioButton supplier;
    @FXML private RadioButton shop;
    @FXML private ComboBox supplierChoice;
    @FXML private TextField nameInput;
    @FXML private TextField usernameInput;
    @FXML private TextField passwordInput;
    @FXML private TextField passwordInput2;
    @FXML private Text errorText;
    
    //HELPER FUNCTIONS
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("login");
    }
    
    //INITIALIZE PAGE 
    public void initialize() throws SQLException {
        ToggleGroup group = new ToggleGroup();
        supplier.setToggleGroup(group);
        shop.setToggleGroup(group);
        
        
        supplierChoice.setDisable(true);
        shop.setSelected(true);
        
        ObservableList<KeyValPair> choices = DatabaseManager.getSuppliers();
        supplierChoice.setItems(choices);
    }
    
    public void radioSupplier() {
        supplierChoice.setDisable(false);
    }
    
    public void radioUser() {
        supplierChoice.setDisable(true);
    }
    
    public void handleSignUp() throws IOException, SQLException {
        String name = nameInput.getText();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        
        // check they cannot be empty otherwise we get sql error
        if (usernameInput.getText().trim().isEmpty() || nameInput.getText().trim().isEmpty() || passwordInput.getText().isEmpty()){
            errorText.setText("Dont leave empty sections!");
            return;
        }
        
        // check username doesn't exist 
        boolean userExists = DatabaseManager.usernameExists(username);
        if (userExists) {
            errorText.setText("Username alreaedy exists, please login!");
        } else if (!(passwordInput.getText().equals(passwordInput2.getText()))){                
            errorText.setText("Passwords do not match!");
             } else {
            // check radio input, if its supplier then get the choicebox index
            errorText.setText("Passwords do not match!");
            if (supplier.isSelected()) {
                KeyValPair output = (KeyValPair) supplierChoice.getValue();
                if (output == null) {
                    errorText.setText("Please select a supplier!");
                } else {
                    DatabaseManager.addUser((name.trim()).toLowerCase(), (username.trim()).toLowerCase(), password, output.getKey());
                    switchToPrimary();
                }
            } else {
                DatabaseManager.addUser((name.trim()).toLowerCase(), (username.trim()).toLowerCase(), password);
                
                if (DatabaseManager.getManagerId() == -1) {
                    User user = DatabaseManager.getUserByUsername(username);
                    DatabaseManager.setManager(user.getUserId());
                    DatabaseManager.approveUser(user.getUserId());
                }
                switchToPrimary();
            }
        }
    }
}