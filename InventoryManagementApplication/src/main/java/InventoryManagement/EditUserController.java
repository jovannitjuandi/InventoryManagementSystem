package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class EditUserController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Text statusInput;
    @FXML private Text suppInput;
    @FXML private Text errorText;
    @FXML private Text userID;
    @FXML private Text deleteMessage;
    @FXML private Text loginUserText;
    @FXML private TextField nameinput;
    @FXML private TextField userNameInput;
    @FXML private ToggleButton approveButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    @FXML private Button delete;
    @FXML private Button dontdelete;
    @FXML private Button logoutButton;
    @FXML private AnchorPane userTable;
    @FXML private AnchorPane deleteConfirmation;

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
        deleteConfirmation.setVisible(false);
        menuBarDisplay();
        previousValue();
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
    }
    
    // HANDLE FUNCTIONS
    public void handleSave() throws SQLException, IOException {
        saveChanges();
    }
    
    public void handleCancel() throws IOException {
        switchToUsers();
    }
    
    public void handleDelete() throws SQLException, IOException {
        //DatabaseManager.removeUser(Common.getIdFromPage());
        //switchToUsers();
        deleteConfirmation.setVisible(true);
        userTable.setVisible(false);
    }
    
    public void handleYesButton() throws SQLException, IOException {
        DatabaseManager.removeUser(Common.getIdFromPage());
        switchToUsers();
    }
    
    public void handleNoButton() throws SQLException{
        deleteConfirmation.setVisible(false);
        userTable.setVisible(true);
    }
    
    public void handleApprove() throws SQLException {
        if (approveButton.isSelected()) {
            approveButton.setText("APPROVED");
        } else {
            approveButton.setText("NOT APPROVED");
        }
    }
    
    public void handleBack() throws IOException, SQLException {
        switchToUsers();
    }

    // HELPER FUNCTIONS
    private void saveChanges() throws SQLException, IOException {
        // get the id from common page
        int id = Common.getIdFromPage();

        // get the new values from the fields
        String name = Common.clean(nameinput.getText());
        String username = Common.clean(userNameInput.getText());
        
        // check all required inputs are filled (display highest priority first)
        boolean noBlank = true;
        if (name.isEmpty() || username.isEmpty() ) {
            noBlank = false;
        }
        if (!noBlank) {
            if (name.isEmpty()) {
                errorText.setText("Name cannot be blank");
                errorText.setVisible(true);
            }

            if (username.isEmpty()) {
                errorText.setText("Username cannot be blank");
                errorText.setVisible(true);
            }
        } else {
            // update the statements
            DatabaseManager.editUser( id,name, username);
            if (approveButton.isSelected()) {
                DatabaseManager.approveUser(id);
            } else {
                DatabaseManager.unapproveUser(id);
            }
            // move to another page
            switchToUsers();
        }
    }
    
    public void confirmDelete() throws IOException{
        deleteMessage.setVisible(true);
        dontdelete.setVisible(true);
        deleteButton.setVisible(true);
        addButton.setVisible(false);
        delete.setVisible(false);
        cancelButton.setVisible(false);

    }

    public void cancelDelete() throws SQLException{
        deleteMessage.setVisible(false);
        dontdelete.setVisible(false);
        deleteButton.setVisible(false);
        addButton.setVisible(true);
        delete.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    private void switchToUsers() throws IOException {
        Common.movePage("user");
    }
    
    private void previousValue() throws SQLException{
        int id = Common.getIdFromPage();
        User current = DatabaseManager.getUserById(id);
        
        //put the values in the fields
        userID.setText(Integer.toString(id));
        nameinput.setText(current.getName());
        userNameInput.setText(current.getUsername());
        suppInput.setText(String.valueOf(current.getSuppId()));
        
        if (current.getApprovedStatus()) {
            approveButton.setText("APPROVED");
            approveButton.setSelected(true);
        } else {
            approveButton.setText("NOT APPROVED");
            approveButton.setSelected(false);
        }
    }
}